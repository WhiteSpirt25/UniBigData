# Задача 1

Нужно написать две программы:
Первая генерирует бинарный файл (min 2Гб), состоящий из случайных 32-рязрядных беззнаковых целых чисел (big endian).
Вторая считает сумму этих чисел (с применением длинной арифметики), находит минимальное и максимальное число.

Реализуйте две версии - 
1. Простое последовательное чтение 
2. Многопоточная + memory-mapped files. Сравните время работы. 