package lesson6;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     *
     * Трудоёмкость O(m*n), m - длина первого слова, n - длина второго
     * Ресурсоёмкость O((m + 1)*(n +1)) - матрица
     */
    public static String longestCommonSubSequence(String first, String second) {
        int[][] matrix = new int [first.length() + 1][second.length() + 1];
        StringBuilder builder = new StringBuilder();
        int m = first.length();
        int n = second.length();

        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                if (first.charAt(i - 1) == second.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }
        while (m > 0 && n > 0) {
            if (first.charAt(m - 1) == second.charAt(n - 1)) {
                builder.append(first.charAt(m - 1));
                m--;
                n--;
            } else if (matrix[m - 1][n] < matrix[m][n - 1]) {
                n--;
            } else m--;
        }
        return builder.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     *
     * Трудоёмкость O(n * log(n), n - длина последовательности
     * Ресурсоёмкость O(n)
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.size() <= 1)
            return list;

        Integer[] minElementForLength = new Integer[list.size()]; // вспомогательная последовательность
        int[] indexes = new int[list.size()]; // в каждой ячейке - размер подпосл-ти, в к-й текущий эл. максимальный - 1
        boolean[] checks = new boolean[list.size()];

        for (int i = 0; i < list.size(); i++) {
            minElementForLength[i] = Integer.MAX_VALUE;
        }

        int maxOfSequence = 0;
        for (int i = 0; i < list.size(); i++) {
            int j = binarySearch(checks, minElementForLength, list.get(i));
            if ((j == 0 || minElementForLength[j - 1] <= list.get(i)) && minElementForLength[j] >= list.get(i)) {
                if (j > indexes[maxOfSequence]) {
                    maxOfSequence = i;
                    indexes[i] = j;
                } else {
                    indexes[i] = j;
                }
                minElementForLength[j] = list.get(i);
                checks[j] = true;
            }
        }

        List<Integer> subSequence = new ArrayList<>();
        int currentIndex = indexes[maxOfSequence] - 1;
        for (int i = 0; i <= currentIndex; i++) {
            subSequence.add(0);
        }
        subSequence.add(list.get(maxOfSequence));

        for (int i = maxOfSequence - 1; i >= 0; i--) {
            int index = indexes[i];
            int value = list.get(i);
            if (index == currentIndex && value <= subSequence.get(index + 1)) {
                currentIndex--;
                subSequence.set(index, value);
            } else if(index > currentIndex && value <= subSequence.get(index + 1)) {
                subSequence.set(index, value);
            }
        }
        return subSequence;
    }

    private static int binarySearch(boolean[] checks, Integer[] minElementForLength, int key) {
        int left = 0;
        int right = minElementForLength.length - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            int compare = minElementForLength[middle].compareTo(key);
            if (compare < 0) {
                left = middle + 1;
            } else if (compare > 0) {
                right = middle - 1;
            } else if (!checks[middle]) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return left;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
