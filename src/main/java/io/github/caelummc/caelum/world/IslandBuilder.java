package io.github.caelummc.caelum.world;

import java.util.Random;

public class IslandBuilder {
    private final int size; // has to be power of two + one
    private final float[] values;

    public IslandBuilder(int size) {
        this.size = size;
        this.values = new float[this.size * this.size * this.size];
    }

    public void set(int x, int y, int z, float value) {
//        if (x >= size) {
//            throw new IndexOutOfBoundsException("Index X " + x + " is out of bounds for size " + size);
//        }
//        if (y >= size) {
//            throw new IndexOutOfBoundsException("Index Y " + y + " is out of bounds for size " + size);
//        }
//        if (z >= size) {
//            throw new IndexOutOfBoundsException("Index Z " + z + " is out of bounds for size " + size);
//        }
        this.values[(x * size + y) * size + z] = value;
    }

    public float get(int x, int y, int z) {
        return this.values[(x * size + y) * size + z];
    }

    // generating islands using midpoint displacement algorithm
    public void generate(Random random) {
        this.set(size / 2, size / 2, size / 2, 1);

        // skipping the zoom = 1, which would initialize center and the centers of faces and edges
        // from the values in eight corners of the cube
        // as Java initializes values with zeroes and central value is set to 1 above
        for (int zoom = 2; zoom < size; zoom *= 2) {
            int step = (size - 1) / zoom;
            float amplitude = 1F / zoom;

            for (int x = 0; x + step < size; x += step) {
                for (int y = 0; y + step < size; y += step) {
                    for (int z = 0; z + step < size; z += step) {
                        this.midpointDisplacement(x, y, z, step, random, amplitude);
                    }
                }
            }

            if (zoom <= 8) {
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        for (int z = 0; z < size; z++) {
                            if (y > size / 2) {
                                // flatten top of the island
                                this.set(x, y, z, this.get(x, y, z) / 2 + random.nextFloat() * 0.1F - 0.1F);
                            } else if (this.get(x, y, z) > 0.15 && random.nextInt(8) == 0) {
                                // add more variety, sometimes
                                this.set(x, y, z, this.get(x, y, z) + 0.25F);
                            }
                        }
                    }
                }
            }
        }
    }

    private void midpointDisplacement(int x0, int y0, int z0, int size, Random random, float amplitude) {
        int x1 = x0 + size;
        int y1 = y0 + size;
        int z1 = z0 + size;
        int centerX = x0 + size / 2;
        int centerY = y0 + size / 2;
        int centerZ = z0 + size / 2;

        float centerValue = (0
                + this.get(x0, y0, z0)
                + this.get(x0, y0, z1)
                + this.get(x0, y1, z0)
                + this.get(x0, y1, z1)
                + this.get(x1, y0, z0)
                + this.get(x1, y0, z1)
                + this.get(x1, y1, z0)
                + this.get(x1, y1, z1)
                + (random.nextFloat() - 0.5F) * amplitude
        ) / 8F;
        this.set(centerX, centerY, centerZ, centerValue);

        for (Face face : Face.values()) {
            int faceCenterX = centerX + size / 2 * face.normal[0];
            int faceCenterY = centerY + size / 2 * face.normal[1];
            int faceCenterZ = centerZ + size / 2 * face.normal[2];

            float total = 0;
            for (int[] corner : face.corners) {
                total += this.get(centerX + size / 2 * corner[0],
                        centerY + size / 2 * corner[1],
                        centerY + size / 2 * corner[2]);
            }
            total += (random.nextFloat() - 0.5F) * amplitude;

            float faceValue = total / 4;
            // more smooth and artifact-free midpoint displacement algorithm
            // would use this center and center of the neighboring cube for the face center...
//                total += centerValue;
//                float faceValue = total / 5;
            this.set(faceCenterX, faceCenterY, faceCenterZ, faceValue);
        }

        for (Edge edge : Edge.values()) {
            int edgeCenterX = centerX + size / 2 * edge.normal[0];
            int edgeCenterY = centerY + size / 2 * edge.normal[1];
            int edgeCenterZ = centerZ + size / 2 * edge.normal[2];

            float total = 0;
            for (int[] end : edge.ends) {
                int endX = centerX + size / 2 * end[0];
                int endY = centerY + size / 2 * end[1];
                int endZ = centerZ + size / 2 * end[2];
                total += this.get(endX, endY, endZ);
            }
            for (Face face : edge.adjacentFaces) {
                int faceCenterX = centerX + size / 2 * face.normal[0];
                int faceCenterY = centerY + size / 2 * face.normal[1];
                int faceCenterZ = centerZ + size / 2 * face.normal[2];
                total += this.get(faceCenterX, faceCenterY, faceCenterZ);
            }
            total += (random.nextFloat() - 0.5F) * amplitude;

            float faceValue = total / 4;
            this.set(edgeCenterX, edgeCenterY, edgeCenterZ, faceValue);
        }

    }

    public enum Axis {
        X, Y, Z;

        public Axis[] getPerpendicularAxes() {
            return switch (this) {
                case X -> new Axis[]{Y, Z};
                case Y -> new Axis[]{X, Z};
                case Z -> new Axis[]{X, Y};
            };
        }

        public static Axis perpendicular(Axis a, Axis b) {
            return switch (a) {
                case X -> b == Y ? Z : Y;
                case Y -> b == X ? Z : X;
                case Z -> b == X ? Y : X;
            };
        }
    }

    public enum Face {
        POS_X(Axis.X, new int[]{1, 0, 0}),
        NEG_X(Axis.X, new int[]{-1, 0, 0}),
        POS_Y(Axis.Y, new int[]{0, 1, 0}),
        NEG_Y(Axis.Y, new int[]{0, -1, 0}),
        POS_Z(Axis.Z, new int[]{0, 0, 1}),
        NEG_Z(Axis.Z, new int[]{0, 0, -1});

        public final Axis axis;
        public final int[] normal;
        public final int[][] corners;

        Face(Axis axis, int[] normal) {
            this.axis = axis;
            this.normal = normal;
            this.corners = new int[4][];
            Axis[] perpAxes = this.axis.getPerpendicularAxes();
            for (int iu = 0; iu < 2; iu++) {
                for (int iv = 0; iv < 2; iv++) {
                    int[] corner = new int[3];
                    corner[this.axis.ordinal()] = normal[this.axis.ordinal()];
                    corner[perpAxes[0].ordinal()] = iu == 0 ? 1 : -1;
                    corner[perpAxes[1].ordinal()] = iv == 0 ? 1 : -1;
                    this.corners[iu * 2 + iv] = corner;
                }
            }
        }
    }

    public enum Edge {
        POS_X_POS_Y(Face.POS_X, Face.POS_Y),
        POS_X_NEG_Y(Face.POS_X, Face.NEG_Y),
        POS_X_POS_Z(Face.POS_X, Face.POS_Z),
        POS_X_NEG_Z(Face.POS_X, Face.NEG_Z),
        NEG_X_POS_Y(Face.NEG_X, Face.POS_Y),
        NEG_X_NEG_Y(Face.NEG_X, Face.NEG_Y),
        NEG_X_POS_Z(Face.NEG_X, Face.POS_Y),
        NEG_X_NEG_Z(Face.NEG_X, Face.NEG_Z),
        POS_Y_POS_Z(Face.POS_Y, Face.POS_Z),
        POS_Y_NEG_Z(Face.POS_Y, Face.NEG_Z),
        NEG_Y_POS_Z(Face.NEG_Y, Face.POS_Z),
        NEG_Y_NEG_Z(Face.NEG_Y, Face.NEG_Z);

        public final Face[] adjacentFaces;
        public final int[] normal;
        public final int[][] ends;

        Edge(Face face1, Face face2) {
            this.adjacentFaces = new Face[]{face1, face2};
            this.normal = new int[]{face1.normal[0] + face2.normal[0], face1.normal[1] + face2.normal[1], face1.normal[2] + face2.normal[2]};

            this.ends = new int[2][];
            for (int i = 0; i < 2; i++) {
                int[] end = new int[]{face1.normal[0] + face2.normal[0], face1.normal[1] + face2.normal[1], face1.normal[2] + face2.normal[2]};
                end[Axis.perpendicular(face1.axis, face2.axis).ordinal()] = i == 0 ? 1 : -1;
                this.ends[i] = end;
            }
        }
    }
}
