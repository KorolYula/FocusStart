## Инструкция по   запуску программы Shapes

Данная программа предназначена для вычисления некоторых характеристик фигуры для дальнейшего печатания в файл или консоль. Параметры фигуры читаются из входного файла.  

Входной файл должен содержать две строки. Первая строка должна содержать код типа фигуры. Вторая строка - параметры фигуры через пробел.
Приложение поддерживает типы фигур и характеристики для них.

Общие характеристики для всех типов фигур:
- Название
- Площадь
-  Периметр

| Название | Тип (код) | Параметры | Выходная информация |
| ----- | -------- | ------ | ----- |
| Круг |  CIRCLE | Радиус |  - Радиус<br/>- Диаметр
| Прямоугольник |  RECTANGLE |  Длины сторон<br/> (два значения) |  - Длина диагонали<br/>- Длина (размер длинной стороны)<br/>- Ширина (размер короткой стороны)
| Треугольник |  TRIANGLE | Длины сторон<br/> (три значения)  |  - Для каждой из трех сторон:<br/>-  Длина стороны и противолежащий угол


Параметры программы задаются при запуске через аргументы командной строки, по порядку:
1. Обязательный аргумент: имя входного файла
2. Необязательный параметр: имя выходного файла. В случае отсутствия этого аргумента программа выведет данные в консоль.

В программе используется логирование в файл  `log/shapes.log`  и в консоль.
