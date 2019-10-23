package lesson1;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

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
    // Трудоёмкость O(n^2), n - число строк в inputName
    // Ресурсоёмкость O(n)
    static public void sortTimes(String inputName, String outputName) {
        try(
                BufferedReader reader = new BufferedReader(new FileReader(inputName));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))
        ) {
            DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("hh:mm:ss a");
            List<LocalTime> dateList = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("([1][0-2]|0[0-9]):[0-5][0-9]:[0-5][0-9]\\s(AM|PM)"))
                    throw new IllegalArgumentException();
                LocalTime date = LocalTime.parse(line, formatter);
                dateList.add(date);
            }

            boolean sorted;
            do {
                sorted = true;
                for (int i = 0; i < dateList.size() - 1; i++) {
                    if (dateList.get(i).compareTo(dateList.get(i + 1)) > 0) {
                        LocalTime date = dateList.get(i);
                        dateList.set(i, dateList.get(i + 1));
                        dateList.set(i + 1, date);
                        sorted = false;
                    }
                }
            } while (!sorted);

            for (LocalTime el: dateList) {
                writer.write(el.format(formatter));
                writer.write("\n");
        }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
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
    // Трудоёмкость O(n(log(N)), n - число строк в inputName
    // Ресурсоёмкость O(n)
    static public void sortAddresses(String inputName, String outputName) {
        List<Address> addressesList;
        Map<Address, List<String>> addressBook = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputName));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("(\\D+\\s?)+-\\s(\\D+\\s?)\\s\\d+"))
                    throw new IllegalArgumentException();

                String[] lineSplit = line.split(" - ");
                Address address = new Address(lineSplit[1]);
                String person = lineSplit[0];

                if (!addressBook.keySet().contains(address)) {
                    addressBook.put(address, new ArrayList<>());
                }

                addressBook.get(address).add(person);
            }

            addressesList = new ArrayList<>(addressBook.keySet());
            Sorts.quickSort(addressesList);

            for (Address address: addressesList) {
                List<String> persons = addressBook.get(address);
                writer.write(address + " - ");
                Sorts.quickSort(persons);

                for (int i = 0; i < persons.size(); i++) {
                    writer.write(persons.get(i));
                    if (i != persons.size() - 1) {
                        writer.write(", ");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            throw  new IllegalArgumentException(e);
        }
    }

     static class Address implements Comparable<Address> {
        private String street;
        private int house;

        Address(String address) {
            street = address.split(" ")[0];
            house = Integer.parseInt(address.split(" ")[1]);
        }

        @Override
        public int hashCode() {
            int result = street.hashCode();
            return result + house;
        }

        @Override
        public boolean equals(Object object) {
            if (object == this)
                return true;
            if (object == null || object.getClass() != this.getClass())
                return false;
            Address address = (Address) object;
            return house == address.house && street.equals(address.street);
        }

        @Override
        public String toString() {
            return street + " " + house;
        }

        @Override
        public int compareTo(@NotNull Address other) {
            if (street.compareTo(other.street) > 0)
                return 1;
            else if (street.compareTo(other.street) == 0)
                    return Integer.compare(house, other.house);
            return -1;
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
    // Трудоёмкость O(n^2), n - число строк в inputName
    // Ресурсоёмкость O(n)
    static public void sortTemperatures(String inputName, String outputName) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputName));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))
        ) {
            List<Double> negativeTemp = new ArrayList<>();
            List<Double> temp = new ArrayList<>();
            Map<Double, Integer> mapTemp = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("-?\\d+\\.\\d+"))
                    throw new IllegalArgumentException();
                double number = Double.valueOf(line);

                if (number < 0 && !negativeTemp.contains(number)) {
                    negativeTemp.add(number);
                }
                if (number >= 0 && !temp.contains(number)) {
                    temp.add(number);
                }
                if (mapTemp.containsKey(number)) {
                    int previous = mapTemp.get(number);
                    mapTemp.put(number, ++previous);
                }
                else {
                    mapTemp.put(number, 1);
                }

            }

            for (int i =0; i < negativeTemp.size(); i++) {
                double current = negativeTemp.get(i);
                int j = i - 1;
                for (; j >= 0; j--) {
                    if (negativeTemp.get(j) > current)
                        negativeTemp.set(j + 1, negativeTemp.get(j));
                    else
                        break;
                }
                negativeTemp.set(j + 1, current);
            }

            for (int i =0; i < temp.size(); i++) {
                double current = temp.get(i);
                int j = i - 1;
                for (; j >= 0; j--) {
                    if (temp.get(j) > current) {
                        temp.set(j + 1, temp.get(j));
                    }
                    else
                        break;
                }
                temp.set(j + 1, current);
            }

            for (Double number: negativeTemp) {
                for (int i = 0; i < mapTemp.get(number); i++) {
                    writer.write(String.valueOf(number));
                    writer.write("\n");
                }
            }
            for (Double number: temp) {
                for (int i = 0; i < mapTemp.get(number); i++) {
                    writer.write(String.valueOf(number));
                    writer.write("\n");
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
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
