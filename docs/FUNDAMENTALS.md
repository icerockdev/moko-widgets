MOKO Widgets позволяет реализовывать UI мобильных приложений из common кода с использованием технологии
 Kotlin Multiplatform.

# Goals
* **1 Kotlin developer => 2 native mobile apps** - позволить Kotlin разработчику быстро создавать типовой UI
 на обе платформы из common кода;
* **Fast start != should be replaced later** - UI созданный из widgets должно быть возможно кастомизировать
 со стороны каждой целевой платформы, без отказа от всего созданного ранее результата. 

# Basic concepts
* **compliance with platform rules** - UI, UX, особенности работы приложения, все это соответстовует
 правилам целевой платформы; 
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

## Compliance with platform rules
### Учитывается пересоздание Activity на Android
```kotlin
expect abstract class Screen<Arg : Args>
```
Класс `Screen` на android является наследником от `Fragment` и корректно восстанвливается стандартными средствами
 Android OS при пересоздании (либо при восстановлении приложения после выгрузки памяти).

Так же `Screen` позволяет получить `ViewModel` из хранилища, как это предполагает нативная разработка:
```kotlin
class DemoScreen() : WidgetScreen<Args.Empty>() {
    
    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {

        val viewModel = getViewModel { DemoViewModel() }

    }

    class DemoViewModel : ViewModel()
}
``` 
При вызове `getViewModel` будет произведена попытка получить уже существующую `ViewModel` из хранилища,
 а если такой нет - создание используя переданную лямбду.

Все это позволяет приложению корректно работать при поворотах экрана, смены языка на устройстве,
 выгрузке приложения из памяти, так же как и обычные нативные android приложения.

### Корректно сохраняется instance state control'ов на Android
Все интерактивные `Widget`ы требуют указание `Id`, благодаря чему корректно работает стандартный механизм
 Android - instance state. Происходит автоматическое сохранение введенных данных, позиции скролла,
 текущий экран навигации при пересозданиях `Activity` и выгрузке приложения из памяти.  

```kotlin
class DemoScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget() = with(theme) {
        scroll(
            id = Id.RootScroll,
            size = WidgetSize.AsParent,
            child = linear(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                children = listOf(
                    input(
                        size = WidgetSize.WidthAsParentHeightWrapContent,
                        id = Id.NameInput,
                        label = const("Ваше имя")
                    )
                )
            )
        )
    }

    object Id {
        object RootScroll : ScrollWidget.Id
        object NameInput : InputWidget.Id
    }
}
```

Любому `Widget` можно указать `Id`, например для стилизации, но для интерактивных `Widget`ов
 указание `Id` обязательно и контролируется на этапе компиляции. 

### Все элементы нативны
Весь получаемый интерфейс является нативным, без собственной отрисовки элементов.  
Например, `NavigationScreen`, который предоставляет навигацию с отображением `Toolbar`/`UINavigationBar`
является полноценнмы `UINavigationController` на iOS, что включает в себя и жесты и анимации.

|Android|iOS|
|---|---|
|![Navigation Android](https://user-images.githubusercontent.com/5010169/69865097-ea351180-12d2-11ea-95c7-bd29e736350f.png)|![Navigation iOS](https://user-images.githubusercontent.com/5010169/69865197-249eae80-12d3-11ea-8f7b-390c641b7ca9.png)|

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

|Android|iOS|
|---|---|
|![Hello world Android](https://user-images.githubusercontent.com/5010169/69857402-84408e00-12c2-11ea-945a-5f287a754e67.png)|![Hello world iOS](https://user-images.githubusercontent.com/5010169/69857202-febcde00-12c1-11ea-8679-5b68b5b11c42.png)|

Как именно будут выглядеть элементы, то есть какие View будут использоваться для отрисовки, 
какие настройки стилизации типа отступов, цветов, шрифтов и подобного, определяется отдельной
сущностью - `ViewFactory`. Через `Theme` мы можем настраивать используемые `ViewFactory`:
```kotlin
val theme = Theme {
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

|Android|iOS|
|---|---|
|![Custom style Android](https://user-images.githubusercontent.com/5010169/69857575-d1bcfb00-12c2-11ea-9cbb-ee6b17357db2.png)|![Custom style iOS](https://user-images.githubusercontent.com/5010169/69857701-09c43e00-12c3-11ea-9e9d-181a298a7edf.png)|

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
### Соответствие виджета и фабрики виджета
Фабрики виджетов привязаны к конкретным виджетам и не могут быть перепутаны.
```kotlin
val theme = Theme {
    textFactory = DefaultTextWidgetViewFactory()
}
```
correct

```kotlin
val theme = Theme {
    textFactory = DefaultContainerWidgetViewFactory()
}
```
incorrect:
>Type mismatch.
 Required: ViewFactory<TextWidget<out WidgetSize>>
 Found: DefaultContainerWidgetViewFactory

### Корректность размеров элементов контейнера
Бывают места, где не подойдет любой размер элемента, например:
- корневой элемент экрана (там логичен только элемент заполняющий все доступное место);
- корневой элемент в ячейке списка (логичны только элементы на всю ширину, но не на всю высоту);
- корневой элемент скролла (логичны только элементы на всю ширину, но не на всю высоту).

```kotlin
override fun createContentWidget() = with(theme) {
    container(size = WidgetSize.AsParent) {
        //...
    }
}
```
correct

```kotlin
override fun createContentWidget() = with(theme) {
    container(size = WidgetSize.WrapContent) {
        //...
    }
}
```
incorrect:
>Return type is  
>ContainerWidget<WidgetSize.Const<SizeSpec.WrapContent, SizeSpec.WrapContent>>'  
>which is not a subtype of overridden  
>Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>   

То есть можно ставить compile-time проверки на корректность размера `Widget`'а, переданного в контейнер-`Widget`.

### Id элемента соответствует типу виджета
```kotlin
container(size = WidgetSize.AsParent, id = Id.RootContainer) {
    //...
}

object Id {
    object RootContainer: ContainerWidget.Id
}
```
Объявленный Id `RootContainer` является идентификатором для `ContainerWidget`
и при работе с ним, мы точно знаем какой виджет будет иметь этот Id.
```kotlin
setContainerFactory(
    DefaultContainerWidgetViewFactory(),
    HelloWorldScreen.Id.RootContainer
)
``` 
valid

```kotlin
setTextFactory(
    DefaultTextWidgetViewFactory(),
    HelloWorldScreen.Id.RootContainer
)
```
invalid:
>Type mismatch.  
 Required: TextWidget.Id  
 Found: HelloWorldScreen.Id.RootContainer

### Экраны явно разделены на имеющие и не имеющие аргументы
```kotlin
class NoArgsScreen : WidgetScreen<Args.Empty>()

class ArgsScreen : WidgetScreen<Args.Parcel<ArgsScreen.Arg>>() {
    @Parcelize
    data class Arg(val id: Int): Parcelable
}
```
При работе с экранами мы точно знаем какой набор аргументов требуется для перехода
на этот экран.

```kotlin
routeToScreen(NoArgsScreen::class)
```
valid

```kotlin
routeToScreen(ArgsScreen::class)
```
invalid
>inferred type ArgsScreen is not a subtype of Screen<Args.Empty>

```kotlin
routeToScreen(ArgsScreen::class, ArgsScreen.Arg(10))
```
valid

```kotlin
object App : BaseApplication() {
    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return ArgsScreen::class
    }
}
```
invalid
>Type mismatch.  
 Required: KClass<out Screen<Args.Empty>>  
 Found: KClass<ArgsScreen>

```kotlin
object App : BaseApplication() {
    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return NoArgsScreen::class
    }
}
```
valid

Только у `ArgsScreen` есть функция получения аргументов:
```kotlin
val id = getArgument().id
```

### Дополнительные требования для перехода на экран
Например `NavigationScreen` реализует навигацию с `UINavigationBar`/`Toolbar` и поэтому требует
реализацию интерфейса `NavigationItem` у экрана, на который происходит переход (для применения
данных к панели навигации).

```kotlin
class DemoScreen : WidgetScreen<Args.Empty>(), NavigationItem {
    override val navigationTitle: StringDesc
        get() = "Demo".desc()
}
```

## Reactive data handling
Каждый `Widget` по-умолчанию принимает данные в виде `LiveData` (портированные в Kotlin Multiplatform из Android
Architecture Components `LiveData` - подробнее в [moko-mvvm](https://github.com/icerockdev/moko-mvvm)).

Как только данные изменятся, на UI это автоматически отобразится.

```kotlin
class TimerScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {
    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val viewModel = getViewModel { TimerViewModel() }

        return with(theme) {
            container(size = WidgetSize.AsParent) {
                center {
                    text(
                        size = WidgetSize.WrapContent,
                        text = viewModel.text
                    )
                }
            }
        }
    }

    class TimerViewModel : ViewModel() {
        private val iteration = MutableLiveData<Int>(0)
        val text: LiveData<StringDesc> = iteration.map { it.toString().desc() }

        init {
            viewModelScope.launch {
                while (isActive) {
                    delay(1000)
                    iteration.value = iteration.value + 1
                }
            }
        }
    }
}
```
В данном примере каждую секунду будет увеличиваться счетчик, который выводится в текст посередине экрана.
Изменение `MutableLiveData`ы `iteration` автоматически обновляет значение `LiveData`ы `text` путем преобразования данных,
 а дальше попадает в `TextWidget` и на экране обновляется значение. 

Для виджетов с вводом данных работает и обратная схема - пользователь вводит текст, а он автоматически попадает
в `MutableLiveData`.

## Styles not a part of structure
Дублирует первый пункт?

## One common declaration may have different platfrom views
Дублирует первый пункт? 

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