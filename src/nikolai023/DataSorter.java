package nikolai023;

import java.io.*;
import java.util.ArrayList;

/**
 * Сортировщик данных.
 * Считывает строки из файла, обрабатывает их в соответствии с полученными аргументами и сортирует.
 * После записывает отсортированные данные в файл, данный в аргументах.
 */
public class DataSorter {
    private ArrayList list = new ArrayList();               //Список объектов, содержащихся в файле
    private String inputFileName;                           //Имя входного файл
    private String outputFileName;                          //Имя файла выводе
    private boolean isInt;                                  //Тип объектов в файле(true - числа, false - строки)
    private boolean isAsc;                                  //Порядок сортировки(true - по возрастанию, false - по убыванию)
    private static DataSorter sorter;

    public DataSorter(String[] args) throws Exception {
        this.parseArgs(args);
    }

    /**
     * Парсер приходящего массива аргументов
     *
     * @param args массив аргументов
     * @throws IllegalArgumentException
     */
    private void parseArgs(String[] args) throws IllegalArgumentException {
        if (args.length < 4) {
            throw new IllegalArgumentException("Ошибка: недостаточно аргументов!");
        }
        this.inputFileName = args[0];
        this.outputFileName = args[1];
        if (args[2].equals("-i")) {
            this.isInt = true;
        } else if (args[2].equals("-s")) {
            this.isInt = false;
        } else {
            throw new IllegalArgumentException("Ошибка: неизвестный аргумент \"" + args[2] + "\"!");
        }
        if (args[3].equals("-a")) {
            this.isAsc = true;
        } else if (args[3].equals("-d")) {
            this.isAsc = false;
        } else {
            throw new IllegalArgumentException("Ошибка: неизвестный аргумент \"" + args[3] + "\"!");
        }
    }

    /**
     * Считывание файла
     *
     * @throws IOException
     */
    private void readFile() throws IOException {
        boolean hasSpaces = false;
        boolean hasEmptyLines = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFileName)))) {
            String str;
            while ((str = reader.readLine()) != null) {
                if (str.contains(" ")) {
                    str = str.trim();
                    hasSpaces = true;
                }
                if (str.equals("")) {
                    hasEmptyLines = true;
                    continue;
                }
                if (this.isInt) {
                    try {
                        this.list.add(Integer.parseInt(str.trim()));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Ошибка: в файле не только числовые данные или число в строке слишком большое: \"" + str + "\"!");
                    }
                } else {
                    this.list.add(str);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Ошибка: файл не найден!");
        } catch (IOException e) {
            throw new IOException("Ошибка: ошибка чтения!");
        }

        if (this.list.size() == 0) {
            throw new EOFException("Ошибка: пустой файл!");
        }
        if (hasEmptyLines) {
            System.out.println("Предупреждение: пустые строки были удалены.");
        }
        if (hasSpaces) {
            System.out.println("Предупреждение: в строке содержится пробел и данные могут быть отсортированы некорректно.");
        }
    }


    /**
     * Сортировка данных
     */
    private void sort() {
        if (this.isAsc) {
            for (int i = 1; i < this.list.size(); i++) {
                Object temp = this.list.get(i);
                int j;
                for (j = i - 1; j >= 0 && compare(temp, this.list.get(j)) < 0; j--) {
                    this.list.set(j + 1, this.list.get(j));
                }
                this.list.set(j + 1, temp);
            }
        } else {
            for (int i = 1; i < this.list.size(); i++) {
                Object temp = this.list.get(i);
                int j;
                for (j = i - 1; j >= 0 && compare(temp, this.list.get(j)) > 0; j--) {
                    this.list.set(j + 1, this.list.get(j));
                }
                this.list.set(j + 1, temp);
            }
        }
    }

    /**
     * Вывод в файл
     *
     * @throws IOException
     */
    private void printFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputFileName)))) {
            for (int i = 0; i < this.list.size() - 1; i++) {
                writer.write(this.list.get(i).toString());
                writer.newLine();
            }
            writer.write(this.list.get(this.list.size() - 1).toString());
        } catch (IOException e) {
            throw new IOException("Ошибка: ошибка записи!");
        }
    }

    /**
     * Функция сравнения элементов
     *
     * @param o1 Первый сравниваемый элемент
     * @param o2 Второй сравниваемый элемент
     * @return Возвращает число, положительное если первый элемент больше лексиграфически(строки) или размерно(числа), 0, если элементы равны или отрицательное, если первый элемент меньше.
     */
    private int compare(Object o1, Object o2) {
        if (this.isInt) {
            Integer i1 = Integer.parseInt((String) o1);
            Integer i2 = Integer.parseInt((String) o2);
            return i1.compareTo(i2);
        } else {
            return ((String) o1).compareTo((String) o2);
        }
    }

    public static void main(String[] args) {
        try {
            sorter = new DataSorter(args);
            sorter.readFile();
            sorter.sort();
            sorter.printFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}