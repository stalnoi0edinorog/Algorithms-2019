package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.*;
import java.util.*;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    // Трудоёмкость O(n)
    // Ресурсоёмкость O(n)
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        List<Integer> prices = new ArrayList<>();
        int minIndex = 0;
        int buy = 0;
        int sell = 0;
        int maxProfit = Integer.MIN_VALUE;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("\\d+"))
                    throw new IllegalArgumentException();
                prices.add(Integer.valueOf(line));
            }

            for (int i = 1; i < prices.size(); i++) {
                if (maxProfit < prices.get(i) - prices.get(minIndex)) {
                    maxProfit = prices.get(i) - prices.get(minIndex);
                    buy = minIndex + 1;
                    sell = i + 1;
                }
                if (prices.get(minIndex) > prices.get(i)) {
                    minIndex = i;
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return new Pair<>(buy, sell);
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    // Трудоёмкость  O(menNumber*choiceInterval)
    // Ресурсоёмкость O(n), n = menNumber
    static public int josephTask(int menNumber, int choiceInterval) {
        ArrayList<Integer> numbers = new ArrayList<>();
        int counter = 0;
        int counterX = 0;

        if (menNumber <= 0 || choiceInterval <= 0)
            throw new IllegalArgumentException();

        if (choiceInterval == 1 || menNumber == 1)
            return menNumber;

        for (int j = 1; j <= menNumber; j++) {
            numbers.add(j);
        }

        int i = 0;
        while (true) {
            for (; i < menNumber; i++) {
                if (numbers.get(i) != -1) {
                    counter++;
                }

                if (counter == choiceInterval) {
                    numbers.set(i, -1);
                    counter = 0;
                    counterX++;
                    break;
                }
            }
            if (counterX == menNumber - 1)
                break;
            i = i  % menNumber;
        }

        for (int j = 0; j < menNumber; j++) {
            if (numbers.get(j) != -1)
                return numbers.get(j);
        }
        return 0;
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    // Трудоёмкость O(n*m), n - длина first, m - длина second
    // Ресурсоёмкость O(n*m)
    static public String longestCommonSubstring(String first, String second) {
        int[][] charsInWords = new int[first.length() + 1][second.length() + 1];
        int maxSubString = -1;
        int begin = -1;
        int end = -1;

        if (first.equals(second))
            return first;

        for (int f = 0; f < first.length(); f++)
            for (int s = 0; s < second.length(); s++) {
                if (f == 0 || s == 0)
                    charsInWords[f][s] = 0;
                else
                    if (first.charAt(f - 1) == second.charAt(s - 1)) {
                        charsInWords[f][s] = charsInWords[f - 1][s - 1] + 1;
                        if (charsInWords[f][s] > maxSubString) {
                            maxSubString = charsInWords[f][s];
                            end = s;
                            begin = s - maxSubString;
                        }
                    }
                    else {
                        charsInWords[f][s] = 0;
                    }
            }

        if (maxSubString == -1)
            return "";

        return second.substring(begin, end);
    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    // Трудоёмкость O(n*n^(1/2))
    // Ресурсоёмкость O(n)
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1)
            return 0;
        int[] numbers = new int[limit];
        int counter = limit - 1;

        for (int k = 1; k < limit; k++) {
            numbers[k] = 1;
        }

        for (int k = 1; k * k <= limit; k++) { // n^(1/2)
            if (numbers[k] == 1) {
                for (int i = (k + 1) * (k + 1); i <= limit; i += k + 1) { // ~n?
                    if (numbers[i - 1] == 0)
                        continue;
                    numbers[i - 1] = 0;
                    counter--;
                }
            }
        }
        return counter;
    }

    /**
     * Балда
     * Сложная
     * <p>
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
