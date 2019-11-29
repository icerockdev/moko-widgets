MOKO Widgets позволяет реализовывать UI мобильных приложений из common кода с использованием технологии
 Kotlin Multiplatform.

# Goals
* **1 Kotlin developer => 2 native mobile apps** - позволить Kotlin разработчику быстро создавать типовой UI
 на обе платформы из common кода;
* **Fast start != should be replaced later** - UI созданный из widgets должно быть возможно кастомизировать
 со стороны каждой целевой платформы, без отказа от всего созданного ранее результата. 

# Basic concepts
* **declare structure, not rendering** - в общем коде определяется структура UI, а отрисовка определяется в
 платформенном коде средствами платформы, что позволяет иметь UI полностью соответствующий целевой платформе,
 но использовать его в общем коде;
* **compile-time safety** - API строготипизировано и почти всё проверяется на этапе компиляции;
* **reactive data handling** - ui связан с данными реактивно, поэтому изменение каких-либо данных автоматически
 отразится в интерфейсе;
* **styles not a part of structure** - стилизация элементов и структура экрана могут быть разделены;
* **one common declaration may have different platfrom views** - объявление одного и того же элемента в
 общем коде может иметь разное визуальное представление на целевых платформах (кнопка в структуре экрана
 всегда остается кнопкой, но на целевых платформах может быть реализовано по разному в разных местах приложения). 
 >“кнопка” не означает “только один вид кнопок“, а означает “что-то, на что можно нажать“. 
 Это можно назвать принципом гибкости представления.

## Declare structure, not rendering
```kotlin
class HelloWorldScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget() = with(theme) {
        container(size = WidgetSize.AsParent) {
            center {
                text(
                    size = WidgetSize.WrapContent,
                    text = const("Hello World!")
                )
            }
        }
    }
}
```
В примере мы определили экран, на котором по центру будет написано "Hello World!".
Как именно будут выглядеть элементы, то есть какие View будут использоваться для отрисовки, 
какие настройки стилизации типа отступов, цветов, шрифтов и подобного, определяется отдельной
сущностью - `ViewFactory`. Через `Theme` мы можем настраивать используемые `ViewFactory`:
```kotlin
val myTheme = Theme {
    textFactory = DefaultTextWidgetViewFactory(
        DefaultTextWidgetViewFactoryBase.Style(
            textStyle = TextStyle(
                size = 24,
                color = Colors.black
            ),
            padding = PaddingValues(padding = 16f)
        )
    )
}
```
Теперь при создании виджетов `text` (`TextWidget`) будет использоваться `DefaultTextWidgetViewFactory` 
(предоставляемая по умолчанию фабрика, для каждого из виджетов есть фабрика по-умолчанию) с заданными
значениями оформления - размер текста 24, цвет текста черный, внутренние отступы в 16 точек (`dp` в Android и `points` в iOS).

Таким образом комбинация `Widget`ов позволяет описать структуру экрана, а указание корректных фабрик позволяет
настраивать визуальное оформление. Фабрики являются `expect`/`actual` классами, то есть каждая целевая платформа должна
реализовать фабрику, в которой будет создание UI элемента из информации в `Widget` классе.

```kotlin
expect class MaterialButtonWidgetViewFactory() : ViewFactory<ButtonWidget<out WidgetSize>>
```
```kotlin
val theme = Theme {
    buttonFactory = MaterialButtonWidgetViewFactory()
}
```
Можно создавать свои фабрики, если возможностей стилизации фабрик по-умолчанию не хватает.
В примере создан `expect` класс `MaterialButtonWidgetViewFactory` и задан как фабрика для всех
`ButtonWidget`. После реализации `actual` версии для `android` мы получаем:

|Default|Material|
|---|---|
|![Default factory](https://user-images.githubusercontent.com/5010169/69856247-cae0b900-12bf-11ea-9230-fb7b3cf39383.png)|![Material factory](https://user-images.githubusercontent.com/5010169/69856326-f9f72a80-12bf-11ea-9b60-7a039f861c4d.png)|

При этом сама структура экрана остается той-же и сам экран не редактируется. Для изменения внешнего вида элементов 
достаточно изменений в теме - указать нужную фабрику элементов.

ЕЩЕ ТУТ МОЖНО ПРО УПРАВЛЕНИЕ ЧЕРЕЗ ID НАПИСАТЬ

## Compile-time safety

 


# Components
Библиотека предоставляет три базовых компонента:
1. `Widget` - описание элемента экрана, из которого целевая платформа создаст итоговую view. Это может
 быть кнопка, список, текст и прочее;
2. `Screen` - отдельный экран приложения;
3. `Application` - само UI приложение. 

## Widget
`Widget` - описание элемента экрана. Это класс содержащий все свойства, которые нужны для создания view
 целевыми платформами.
`WidgetViewFactory` - фабрика view, которая определяется отдельно на каждой целевой платформе.

## Screen
`Screen` является `expect`/`actual` классом. Для Android `Screen` = `Fragment` (androidx), а для iOS - фабрикой
 `UIViewController`'а (так как в Kotlin/Native нельзя делать not-final классы-наследники от ObjC классов).

|Common|Android|iOS|
|---|---|---|
|expect Screen|actual Screen: Fragment|actual Screen { fun createViewController() }|

## Application
`BaseApplication` это абстрактный класс, представляющий стартовую точку Multiplatform UI для каждой целевой
 платформы. Наследник данного класса должен предоставлять информацию о стартовом экране, а так же регистрировать
 фабрики всех экранов.

# Some Android issues addressed in moko-widgets
MOKO Widgets fixes a series of issues that Android suffers from:
* Экраны (активности/фрагменты) должны работать через конструктор по умолчанию.