package ru.korol;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    private final static int TREADS_COUNT = 10;

    private static double result;

    private static class Task implements Callable<Double> {
        final long begin;
        final long end;

        public Task(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public Double call() {
            double result = 0.0;
            for (long j = begin; j <= end; j++) {
                result += 1.0 / (j * j);
            }
            return result;
        }
    }

    public static long getNumber() throws TaskException {
        long number;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите целое число больше 1: ");
        try {
            number = scanner.nextLong();

            if (number <= 1) {
                throw new TaskException("Ввели число " + number + " < 1 ");
            }

            log.info("Прочитана цифра {}", number);
            return number;

        } catch (InputMismatchException e) {
            throw new TaskException("Ввели не число");
        }
    }

    public static void main(String[] args) {
        try {
            long number = getNumber();

            long step = number / (TREADS_COUNT - 1);
            int treadCount = TREADS_COUNT;

            if (number < TREADS_COUNT) {
                treadCount = 1;
                step = number;
                log.info("Ввели число меньше, чем заданное количество потоков.Все вычисления проводим в одном потоке");
            }

            log.info("Шаг для каждого потока {} ", step);

            ExecutorService executor = Executors.newFixedThreadPool(treadCount);

            List<Future<Double>> list = new ArrayList<>();

            for (int i = 0; i < treadCount; i++) {
                long begin = i * step + 1;
                long end = Math.min(number, (i + 1) * step);
                Task callableTask = new Task(begin, end);
                Future<Double> future = executor.submit(callableTask);
                list.add(future);
                log.info("Поток {}. Границы от {} до {}", i, begin, end);
            }

            for (Future<Double> fut : list) {
                result += fut.get();
            }

            executor.shutdown();
            System.out.println(result);

        } catch (TaskException e) {
            log.error("Обработанная ошибка в процессе выполнения программы: " + e.getMessage());
        } catch (Exception e) {
            log.error("Ошибка в процессе выполнения программы " + e);
        }
    }
}
