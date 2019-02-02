val API_URL = js("getApiUrl()") as String

fun main(args: Array<String>) {
    val BookStorePresenter = BookStorePresenter()
    val BookStorePage = BookStorePage(BookStorePresenter)
    BookStorePage.show()
    BookStorePresenter.attach(BookStorePage)
    BookStorePresenter.loadBooks()
}

