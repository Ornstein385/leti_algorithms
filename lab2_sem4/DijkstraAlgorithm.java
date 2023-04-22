public class DijkstraAlgorithm {
    private static final int NO_PARENT = -1;

    public static void dijkstra(int[][] adjacencyMatrix, int startVertex) {
        if (adjacencyMatrix.length == 0){
            return;
        }
        int nVertices = adjacencyMatrix[0].length;
        int[] shortestDistances = new int[nVertices];
        boolean[] visited = new boolean[nVertices];

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            visited[vertexIndex] = false;
        }

        shortestDistances[startVertex] = 0;

        BinaryHeap heap = new BinaryHeap();

        heap.push(startVertex);

        while (!heap.isEmpty()) {
            int currentVertex = heap.pop();
            visited[currentVertex] = true;

            for (int neighborIndex = 0; neighborIndex < nVertices; neighborIndex++) {
                int edgeDistance = adjacencyMatrix[currentVertex][neighborIndex];

                if (edgeDistance > 0 && !visited[neighborIndex]) {
                    int newShortestDistance = shortestDistances[currentVertex] + edgeDistance;

                    if (newShortestDistance < shortestDistances[neighborIndex]) {
                        shortestDistances[neighborIndex] = newShortestDistance;
                        heap.push(neighborIndex);
                    }
                }
            }
        }
        //printSolution(startVertex, shortestDistances);
    }

    private static void printSolution(int startVertex, int[] distances) {
        int nVertices = distances.length;
        System.out.print("Vertex\t Distance from Source Vertex\n");
        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            if (vertexIndex != startVertex) {
                System.out.print(vertexIndex + "\t\t" + distances[vertexIndex] + "\n");
            }
        }
    }
}