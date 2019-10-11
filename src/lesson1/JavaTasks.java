package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    // трудоёмкость O(n^2)
    static public void sortTimes(String inputName, String outputName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("hh:mm:ss a");
            List<LocalTime> dateList = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("([1][0-2]|0[0-9]):[0-5][0-9]:[0-5][0-9]\\s(AM|PM)"))
                    throw new IllegalArgumentException();
                LocalTime date = LocalTime.parse(line, formatter);
                dateList.add(date);
            }

            boolean sorted = false;
            while (!sorted) {
            sorted = true;
            for (int i = 0; i < dateList.size() - 1; i++) {
                if (dateList.get(i).compareTo(dateList.get(i + 1)) > 0) {
                    LocalTime date = dateList.get(i);
                    dateList.set(i, dateList.get(i + 1));
                    dateList.set(i + 1, date);
                    sorted = false;
                }
            }
        }

            for (LocalTime el: dateList) {
                writer.write(el.format(formatter));
                writer.write("\n");
        }
            reader.close();
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    //Трудоёмкссть O(n^2)
    static public void sortAddresses(String inputName, String outputName) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
            Map<String, ArrayList<Integer>> streetsAndHouses = new HashMap<>();
            Map<String, ArrayList<String>> streetsAndPersons = new HashMap<>();
            List<String> housesSorted = new ArrayList<>();
            List<String> addressSorted = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("(\\D+\\s?)+-\\s(\\D+\\s?)\\s\\d+"))
                    throw new IllegalArgumentException();

                String address = line.split("\\s-\\s")[1];
                String street = address.split("\\s")[0];
                Integer houseNumber = Integer.valueOf(address.split("\\s")[1]);
                String person = line.split("\\s-\\s")[0];

                if (!streetsAndHouses.containsKey(street))
                    streetsAndHouses.put(street, new ArrayList<>());
                if (!streetsAndHouses.get(street).contains(houseNumber))
                    streetsAndHouses.get(street).add(houseNumber);
                if (!streetsAndPersons.containsKey(address))
                    streetsAndPersons.put(address, new ArrayList<>());

                streetsAndPersons.get(address).add(person);

                if (!housesSorted.contains(street))
                    housesSorted.add(street);
            }


            for (Map.Entry<String, ArrayList<String>> pair: streetsAndPersons.entrySet()){
                Collections.sort(pair.getValue());
            }

            Collections.sort(housesSorted);

            for (Map.Entry<String, ArrayList<Integer>> pair: streetsAndHouses.entrySet())
                Collections.sort(pair.getValue());

            for (String street: housesSorted) {
                for (Integer i: streetsAndHouses.get(street)) {
                    addressSorted.add(street + " " + i);
                }
            }

            for (String address: addressSorted) {
                writer.write(address + " - ");
                for (String name : streetsAndPersons.get(address)) {
                    writer.write(name);
                    if (!name.equals(streetsAndPersons.get(address).get(streetsAndPersons.get(address).size() - 1)))
                        writer.write(", ");
                }
                writer.write("\n");
            }

            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    //Трудоёмкость O(n^2)
    static public void sortTemperatures(String inputName, String outputName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));
            List<Double> listOfNumbers = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("-?\\d+\\.\\d+"))
                    throw new IllegalArgumentException();
                double number = Double.valueOf(line);

                if (listOfNumbers.isEmpty()) {
                    listOfNumbers.add(number);
                    continue;
                }
                if (listOfNumbers.size() == 1) {
                    if (listOfNumbers.get(0) <= number)
                        listOfNumbers.add(number);
                    else
                        listOfNumbers.add(0, number);
                    continue;
                }
                if (number > listOfNumbers.get(listOfNumbers.size() - 1)) {
                    listOfNumbers.add(number);
                    continue;
                }
                if (number < listOfNumbers.get(0)) {
                    listOfNumbers.add(0, number);
                    continue;
                }
                if (listOfNumbers.contains(number)) {
                    listOfNumbers.add(listOfNumbers.indexOf(number) + 1, number);
                    continue;
                }
                for (int i = 0; i < listOfNumbers.size() - 1; i++) {
                    if (number < listOfNumbers.get(i + 1) && number > listOfNumbers.get(i)) {
                        listOfNumbers.add(i + 1, Double.valueOf(line));
                        break;
                    }
                }
            }

            for (Double number: listOfNumbers) {
                writer.write(String.valueOf(number));
                writer.write("\n");
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
