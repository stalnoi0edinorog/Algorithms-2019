package lesson5;

import kotlin.NotImplementedError;
import lesson5.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     *
     * Трудоёмкость O(E + V)
     * Ресурсоёмкость O(E + V)
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        Set<Graph.Edge> edges = graph.getEdges();
        Set<Graph.Vertex> vertices = graph.getVertices();

        if (vertices.isEmpty() || !isEulerGraph(graph))
            return new ArrayList<>();

        List<Graph.Edge> result = new ArrayList<>();
        Iterator<Graph.Vertex> iterator = graph.getVertices().iterator();
        Stack<Graph.Vertex> verticesStack = new Stack<>();
        verticesStack.add(iterator.next());

        while (!verticesStack.isEmpty()) {
            Graph.Vertex currentVertex = verticesStack.peek();

            for (Graph.Vertex vertex: graph.getVertices()) {
                Graph.Edge edge = graph.getConnection(currentVertex, vertex);
                if (edge == null)
                    continue;
                if (edges.contains(edge)) {
                    verticesStack.push(vertex);
                    edges.remove(edge);
                    break;
                }
            }
            if (currentVertex == verticesStack.peek()) {
                verticesStack.pop();
                if (!verticesStack.isEmpty()) {
                    result.add(graph.getConnection(currentVertex, verticesStack.peek()));
                }
            }
        }
        return result;
    }

    private static boolean isEulerGraph(Graph graph) {
        for (Graph.Vertex vertex: graph.getVertices()) {
            if (graph.getNeighbors(vertex).size() % 2 != 0)
                return false;
        }
        return true;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     *
     * Трудоёмкость O(E + V)
     * Ресурсоёмкость O(E + V)
     */
    public static Graph minimumSpanningTree(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();
        GraphBuilder spanningTree = new GraphBuilder();

        if (graph.getVertices().isEmpty())
            return spanningTree.build();

        Iterator<Graph.Vertex> iterator = vertices.iterator();
        Graph.Vertex begin = iterator.next();
        Map<Graph.Vertex, VertexInfo> info = DijkstraKt.shortestPath(graph, begin);

        for (Graph.Vertex vertex: graph.getVertices()) {
            spanningTree.addVertex(vertex.getName());
        }

        for (Map.Entry<Graph.Vertex, VertexInfo> pair: info.entrySet()) {
            if (pair.getValue().getPrev() != null) {
                spanningTree.addConnection(pair.getValue().getPrev(), pair.getKey(), 1);
            }
        }
        return spanningTree.build();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     *
     * Трудоёмкость O(V^2)
     * Ресурсоёмкость O(E + V)
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> vertices = graph.getVertices();

        if (vertices.isEmpty())
            return new HashSet<>();

        if (isCycle(graph))
            throw  new IllegalArgumentException();

        List<Set<Graph.Vertex>> independentVertexSet = new ArrayList<>();

        for (Graph.Vertex vertex: vertices) {
            Set<Graph.Vertex> first = new HashSet<>(); // добавляемые вершины
            Set<Graph.Vertex> second = new HashSet<>(); // соседи добавляемых вершин

            for (Graph.Vertex anotherVertex: vertices) {
                if (!graph.getNeighbors(vertex).contains(anotherVertex) &&
                        !second.contains(anotherVertex)) {
                    second.addAll(graph.getNeighbors(anotherVertex));
                    first.add(anotherVertex);
                }
            }
            if (!independentVertexSet.contains(first))
                independentVertexSet.add(first);
        }

        Set<Graph.Vertex> largestIndependentVertexSet = new HashSet<>();
        for (Set<Graph.Vertex> max : independentVertexSet) {
            if (largestIndependentVertexSet.size() < max.size()) {
                largestIndependentVertexSet = max;
            }
        }
        return largestIndependentVertexSet;
    }

    private static boolean isCycle(Graph graph) {
        Set<Graph.Vertex> visitedVertices = new HashSet<>();
        Set<Graph.Edge> visitedEdges = new HashSet<>();

        class DFS {
            private boolean dfs(Graph.Vertex vertex) {
                if (visitedVertices.contains(vertex))
                    return true;
                visitedVertices.add(vertex);
                for (Map.Entry<Graph.Vertex, Graph.Edge> pair: graph.getConnections(vertex).entrySet()) {
                    if (!visitedEdges.contains(pair.getValue())) {
                        visitedEdges.add(pair.getValue());
                        if (dfs(pair.getKey()))
                            return true;
                    }

                }
                return false;
            }
        }
        DFS research = new DFS();
        return research.dfs(graph.getVertices().iterator().next());
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path longestSimplePath(Graph graph) {
        throw new NotImplementedError();
    }
}
