import org.w3c.dom.*
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.addClass

interface BookStoreContract {
    interface View {
        fun showBooks(books: List<Book>)
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter {
        fun attach(view: View)
        fun loadBooks()
    }
}

class BookStorePage(private val presenter: BookStoreContract.Presenter) :
        BookStoreContract.View {
    override fun showBooks(books: List<Book>) {
        books.forEach { book ->
            val card = cardBuilder.build(book)
            content.appendChild(card)
        }
    }

    override fun showLoader() {
        loader.style.visibility = "visible"
    }

    override fun hideLoader() {
        loader.style.visibility = "hidden"
    }

    private val loader = document.getElementById("loader") as HTMLDivElement
    private val content = document.getElementById("content") as HTMLDivElement
    private val cardBuilder = CardBuilder()

    fun show() {
        presenter.attach(this)
        presenter.loadBooks()
    }
}


class BookStorePresenter : BookStoreContract.Presenter {
    private lateinit var view: BookStoreContract.View

    override fun attach(view: BookStoreContract.View) {
        this.view = view
    }

    override fun loadBooks() {
        view.showLoader()
        getAsync(API_URL) { response ->
            val books = JSON.parse<Array<Book>>(response)

            view.hideLoader()

            view.showBooks(books.toList())
        }
    }

    private fun getAsync(url: String, callback: (String) -> Unit) {
        val xmlHttp = XMLHttpRequest()
        xmlHttp.open("GET", url)
        xmlHttp.onload = {
            if (xmlHttp.readyState == 4.toShort() && xmlHttp.status == 200.toShort()) {
                callback.invoke(xmlHttp.responseText)
            }
        }

        xmlHttp.send()
    }
}


class CardBuilder {
    fun build(book: Book): HTMLElement {
        val containerElement = document.createElement("div") as HTMLDivElement
        val imageElement = document.createElement("img") as HTMLImageElement
        val titleElement = document.createElement("div") as HTMLDivElement
        val priceElement = document.createElement("div") as HTMLDivElement
        val descriptionElement = document.createElement("div") as HTMLDivElement
        val viewDetailsButtonElement = document.createElement("button") as HTMLButtonElement

        bind(book = book,
                imageElement = imageElement,
                titleElement = titleElement,
                priceElement = priceElement,
                descriptionElement = descriptionElement,
                viewDetailsButtonElement = viewDetailsButtonElement)

        applyStyle(containerElement,
                imageElement = imageElement,
                titleElement = titleElement,
                priceElement = priceElement,
                descriptionElement = descriptionElement,
                viewDetailsButtonElement = viewDetailsButtonElement)

        containerElement
                .appendChild(
                        imageElement,
                        titleElement,
                        priceElement,
                        descriptionElement,
                        viewDetailsButtonElement
                )
        return containerElement
    }

    private fun Element.appendChild(vararg elements: Element) {
        elements.forEach {
            this.appendChild(it)
        }
    }

    private fun bind(book: Book,
                     imageElement: HTMLImageElement,
                     titleElement: HTMLDivElement,
                     priceElement: HTMLDivElement,
                     descriptionElement: HTMLDivElement,
                     viewDetailsButtonElement: HTMLButtonElement) {


        imageElement.src = book.coverUrl

        titleElement.innerHTML = book.title
        priceElement.innerHTML = book.price
        descriptionElement.innerHTML = book.description
        viewDetailsButtonElement.innerHTML = "view details"

        viewDetailsButtonElement.addEventListener("click", {
            window.open(book.url)
        })
    }

    private fun applyStyle(containerElement: HTMLDivElement,
                           imageElement: HTMLImageElement,
                           titleElement: HTMLDivElement,
                           priceElement: HTMLDivElement,
                           descriptionElement: HTMLDivElement,
                           viewDetailsButtonElement: HTMLButtonElement) {
        containerElement.addClass("card", "card-shadow")
        imageElement.addClass("cover-image")
        titleElement.addClass("text-title", "float-left")
        descriptionElement.addClass("text-description", "float-left")
        priceElement.addClass("text-price", "float-left")
        viewDetailsButtonElement.addClass("view-details", "ripple", "float-right")
    }

}
