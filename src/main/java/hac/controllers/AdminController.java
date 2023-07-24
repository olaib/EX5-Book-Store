package hac.controllers;

import hac.repo.Book;
import hac.repo.Payment;
import hac.services.BookService;
import hac.services.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * admin controller just admin allowed
 */
@RequestMapping("/admin")
@Controller
public class AdminController {
    /**
     * c-tor
     *
     * @param bookService    bookService autowired
     * @param paymentService payment autowired
     */
    @Autowired
    AdminController(BookService bookService, PaymentService paymentService) {
        this.bookService = bookService;
        this.paymentService = paymentService;
    }

    /**
     * book service
     */
    private BookService bookService;
    /**
     * payment service
     */
    private PaymentService paymentService;
    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(AdminController.class);

    /**
     * index page
     *
     * @param model model to be added
     * @return index page with books and payments
     */
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("books", bookService.getBooks());
        model.addAttribute("payments", paymentService.getPayments());
        return "admin/index";
    }

    /**
     * login page for admin
     *
     * @return login page for admin
     */

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    /**
     * return updating book page by id
     *
     * @param id    id of the book
     * @param model model to be added
     * @return update book page
     */
    @PostMapping("/edit-book")
    public String updateBook(@RequestParam("id") long id, Model model) {
        Book book = bookService.getBook(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "update-book";
    }

    /**
     * handling update book post
     *
     * @param id     id of the book
     * @param book   book to be updated
     * @param result result
     * @return redirect to admin page
     */
    @PostMapping("/update-book/{id}")
    public String updateBook(@PathVariable long id, @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            book.setId(id);
            return "update-book";
        }
        bookService.saveBook(book);
        return "redirect:/admin/";
    }

    /**
     * return updating payment page by id
     *
     * @param id    id of the payment
     * @param model model to be added
     * @return update payment page
     */
    @PostMapping("/edit-payment")
    public String updatePay(@RequestParam("id") long id, Model model) {
        Payment payment = paymentService.getPayment(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
        model.addAttribute("payment", payment);
        return "update-payment";
    }

    /**
     * handling update payment post
     *
     * @param id      id of the payment
     * @param payment payment to be updated
     * @param result  result
     * @return redirect to admin page
     */
    @PostMapping("/update-payment/{id}")
    public String updatePay(@PathVariable long id, @Valid Payment payment, BindingResult result) {
        if (result.hasErrors()) {
            payment.setId(id);
            return "update-payment";
        }
        paymentService.recordNewPayment(payment);
        return "redirect:/admin/";
    }

    /**
     * delete book by id
     *
     * @param id    id of the book
     * @param model model to be added
     * @return redirect to admin page
     */

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id, Model model) {
        Book book = bookService.getBook(id).orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        bookService.deleteBook(book);
        return "redirect:/admin/";
    }

    /**
     * delete payment by id
     *
     * @param id    id of the payment
     * @param model model to be added
     * @return redirect to admin page
     */

    @PostMapping("/delete-payment/{id}")
    public String deletePay(@PathVariable long id, Model model) {
        Payment payment = paymentService.getPayment(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
        paymentService.deletePay(payment);
        return "redirect:/admin/";
    }

    /**
     * add book post
     *
     * @param book   book to be added
     * @param result result
     * @param model  model to be added
     * @return redirect to admin page
     */
    @PostMapping("/add-book")
    public String addBook(@Valid Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-book";
        }
        bookService.saveBook(book);
        return "redirect:/admin/";
    }

    /**
     * get add book page
     *
     * @param book  book to be added
     * @param model model to be added
     * @return add book page
     */
    @GetMapping("add-book")
    public String showBookForm(Book book, Model model) {
        return "add-book";
    }

    /**
     * add payment post
     *
     * @param payment payment to be added
     * @param result  result
     * @param model   model to be added
     * @return redirect to admin page
     */

    @PostMapping("/add-payment")
    public String addPay(@Valid Payment payment, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-payment";
        }
        paymentService.recordNewPayment(payment);
        return "redirect:/admin/";
    }

    /**
     * get add payment page
     *
     * @param payment payment to be added
     * @param model   model to be added
     * @return add payment page
     */
    @GetMapping("add-payment")
    public String showPayForm(Payment payment, Model model) {
        return "add-payment";
    }

    /**
     * Error page that displays all exceptions.
     */
    @RequestMapping("/errorpage")
    public String error(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * simple Error page.
     */
    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    /**
     * Error page that displays all exceptions.
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model) {

        //logger.error("Exception during execution of SpringSecurity application", ex);
        String errorMessage = (ex != null ? ex.getMessage() : "Unknown error");

        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}