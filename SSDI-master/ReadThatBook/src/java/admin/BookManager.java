/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import business.Book;
import business.Rating;
import business.User;
import data.BookDB;
import data.RatingDB;
import data.UserDB;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author Prateek
 */
@MultipartConfig
public class BookManager extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request == null || response == null) {
            return;
        }

        String action = request.getParameter("action");
        //String userStr = request.getParameter("user");
        HttpSession session = request.getSession();

        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userCookie")) {
                    user = UserDB.selectUser(cookie.getValue());
                    request.setAttribute("user", user);
                }
            }
        }

        String url = "/index.jsp";
        String message = "";
        //User user = UserDB.selectUser(userStr);
        //System.out.println(user.getUserName() + " " + user.getRole());
        System.out.println("admin.BookManager.doPost()" + action);
        if (action == null) {
            action = "viewHome";
        } else if (action.equals("addBook")) {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String ISBN_13Str = request.getParameter("ISBN_13");
            String ISBN_10Str = request.getParameter("ISBN_10");
            System.out.println("admin.BookManager.doPost()" + ISBN_13Str);
            System.out.println("admin.BookManager.doPost()" + ISBN_10Str);
            int ISBN_10 = 0;
            long ISBN_13 = 0;
            String messageText;
            InputStream inputStream = null;
            //Collection coll = request.getParts();
            Part filePart = request.getPart("photo");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            if (filePart != null) {
                System.out.println(filePart.getName());
                System.out.println(filePart.getContentType());

                inputStream = filePart.getInputStream();
            }
            try {
                ISBN_10 = Integer.parseInt(ISBN_10Str);
                System.out.println("inside try for ISBN_10Str");

            } catch (Exception e) {
                System.out.println("admin.BookManager.doPost()" + e);
            }
            try {
                ISBN_13 = Long.parseLong(ISBN_13Str);
                System.out.println("inside try for ISBN_13Str");

            } catch (Exception e) {
                System.out.println("admin.BookManager.doPost()" + e);
            }
            String author = request.getParameter("author");
            String genre = request.getParameter("genre");
            String edition = request.getParameter("edition");
            String publisher = request.getParameter("publisher");
            System.out.println("admin.BookManager.doPost()" + title);
            System.out.println("admin.BookManager.doPost()" + description);
            System.out.println("admin.BookManager.doPost()" + ISBN_13);
            System.out.println("admin.BookManager.doPost()" + ISBN_10);
            System.out.println("admin.BookManager.doPost()" + author);
            System.out.println("admin.BookManager.doPost()" + genre);
            System.out.println("admin.BookManager.doPost()" + edition);
            System.out.println("admin.BookManager.doPost()" + publisher);

            Book newBook = new Book(title, author, ISBN_10Str, ISBN_13Str, genre, edition, publisher, description);
            int status = BookDB.addBook(newBook);
            if (status == 0) {
                messageText = "Error in book addition . Please go through log";
                url = "/addBooks.jsp";
                request.setAttribute("messageText", messageText);
                request.setAttribute("user", user);
                getServletContext().getRequestDispatcher(url).forward(request, response);

            }
            int bookID = 0;
            if (ISBN_10Str != null) {
                bookID = BookDB.getBookIDByISBN(ISBN_10Str);
            } else if (ISBN_13Str != null) {
                bookID = BookDB.getBookIDByISBN(ISBN_13Str);
            }

            // String addedBookID = BookDB.selectBook(ISBN_13)
            if (inputStream != null && bookID != 0) {
                BookDB.addBookImage(bookID, inputStream);
            }
            url = "/manageBooks.jsp";
            List<Book> bookList = BookDB.selectAllBooks();
            request.setAttribute("bookList", bookList);
            //byte[] imageBytes = getImageAsBytes();

//            response.setContentType("image/jpeg");
//            response.setContentLength(imageBytes.length);
//
//            response.getOutputStream().write(imageBytes);
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("deleteBook")) {
            String bookIDStr = request.getParameter("bookID");
            System.out.println("admin.BookManager.doPost()" + " bookID = " + bookIDStr);
            int bookID = -1;
            System.out.println("admin.BookManager.doPost()" + " deleteBook");
            try {
                bookID = Integer.parseInt(bookIDStr);
                System.out.println("admin.BookManager.doPost()" + " bookIDInt = " + bookID);
            } catch (Exception e) {
                System.out.println(e);
                url = "/home.jsp";
                //request.setAttribute("booklist", booklist);
                request.setAttribute("user", user);
                getServletContext().getRequestDispatcher(url).forward(request, response);
            }
            BookDB.deleteBook(bookID);
            url = "/manageBooks.jsp";
            //request.setAttribute("user", user);
            List<Book> booklist = BookDB.selectAllBooks();
            request.setAttribute("bookList", booklist);
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);

        } else if (action.equals("manageBooks")) {
            url = "/manageBooks.jsp";
            List<Book> bookList = BookDB.selectAllBooks();

            request.setAttribute("bookList", bookList);
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("addBookPage")) {
            url = "/addBooks.jsp";
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("updateBookPage")) {
            url = "/updateBook.jsp";
            String bookIDStr = request.getParameter("bookID");
            System.out.println("Inside updateBookPage");
            System.out.println("bookID is " + bookIDStr);
            int bookID = 0;
            try {
                bookID = Integer.parseInt(bookIDStr);
            } catch (Exception e) {
                System.out.println(e);
            }
            Book updatableBook = BookDB.selectBook(bookID);
            System.out.println("book selected for update" + updatableBook.getTitle());
            request.setAttribute("book", updatableBook);
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("updateBook")) {
            String title = request.getParameter("title");
            String bookIDStr = request.getParameter("bookID");
            String description = request.getParameter("description");
            String ISBN_13Str = request.getParameter("ISBN_13");
            String ISBN_10Str = request.getParameter("ISBN_10");
            System.out.println("admin.BookManager.doPost()" + ISBN_13Str);
            System.out.println("admin.BookManager.doPost()" + ISBN_10Str);
            int ISBN_10 = 0;
            long ISBN_13 = 0;
            InputStream inputStream = null;
            int bookID = 0;
            try {
                bookID = Integer.parseInt(bookIDStr);
            } catch (Exception e) {
                System.out.println(e);
            }
            //Collection coll = request.getParts();
            Part filePart = request.getPart("photo");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            if (filePart != null) {
                System.out.println("FilePart name:" + filePart.getName());
                System.out.println("FilePart content" + filePart.getContentType());

                System.out.println("FileName : " + filePart.getSubmittedFileName());
                System.out.println("FileName isEmpty? : " + filePart.getSubmittedFileName().isEmpty());

                inputStream = filePart.getInputStream();
                System.out.println("inPut stream : " + inputStream);
            }
            try {
                ISBN_10 = Integer.parseInt(ISBN_10Str);
                System.out.println("inside try for ISBN_10Str");

            } catch (Exception e) {
                System.out.println("admin.BookManager.doPost()" + e);
            }
            try {
                ISBN_13 = Long.parseLong(ISBN_13Str);
                System.out.println("inside try for ISBN_13Str");

            } catch (Exception e) {
                System.out.println("admin.BookManager.doPost()" + e);
            }
            String author = request.getParameter("author");
            String genre = request.getParameter("genre");
            String edition = request.getParameter("edition");
            String publisher = request.getParameter("publisher");
            System.out.println("Title" + title);
            System.out.println("description" + description);
            System.out.println("ISBN_13" + ISBN_13);
            System.out.println("ISBN_10 " + ISBN_10);
            System.out.println("author" + author);
            System.out.println("genre" + genre);
            System.out.println("edition" + edition);
            System.out.println("publisher" + publisher);

            Book updateBook = new Book(title, author, ISBN_10Str, ISBN_13Str, genre, edition, publisher, description);
            updateBook.setBookID(bookID);
            BookDB.updateBook(updateBook);
            // String addedBookID = BookDB.selectBook(ISBN_13)
            if (!filePart.getSubmittedFileName().isEmpty()) {
                System.out.println("Inside inputStream != null");
                if (BookDB.checkBookImageExists(bookID)) {
                    BookDB.updateBookImage(bookID, inputStream);
                    System.out.println("updateBookImage method called in servlet ");
                } else {
                    BookDB.addBookImage(bookID, inputStream);
                    System.out.println("addBookImage method called in servlet ");
                }
            }
            url = "/manageBooks.jsp";
            List<Book> bookList = BookDB.selectAllBooks();
            request.setAttribute("bookList", bookList);
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("viewBooks")) {
            byte[] b = BookDB.getBookImage(8);
            OutputStream img = null;
            response.setContentType("image/png");
            img = response.getOutputStream();
            img.write(b);
            img.flush();
            img.close();
        } else if (action.equals("searchBook")) {
            String bookName = request.getParameter("searchString");
            List<Book> bookList = BookDB.searchBook(bookName);
            if (bookList.size() == 0) {
                System.out.println("admin.BookManager.doPost()" + "Book Does not exist");
                cookies = request.getCookies();

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userCookie")) {
                        url = "/home.jsp";
                        user = UserDB.selectUser(cookie.getValue());
                        request.setAttribute("user", user);
                    } else {
                        url = "/guestHome.jsp";
                    }
                }

                message = "Book does not exist with this title or author.";
                request.setAttribute("searchErrorMessage", message);
                List<Book> books = BookDB.selectAllBooks();
                request.setAttribute("books", books);
                request.setAttribute("user", user);
                request.getServletContext().getRequestDispatcher(url).forward(request, response);
            } else {
                url = "/viewSearchResult.jsp";
                request.setAttribute("bookResult", bookList);
                request.setAttribute("user", user);
                getServletContext().getRequestDispatcher(url).forward(request, response);
            }
        } else if (action.equals("advancedSearchBook")) {
            url = "/advancedSearch.jsp";
            request.setAttribute("user", user);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else if (action.equals("advSearchBook")) {
            String bookTitle = request.getParameter("searchBookTitle");
            String bookAuthor = request.getParameter("searchBookAuthor");
            String bookGenre = request.getParameter("searchBookGenre");
            String bookISBN = request.getParameter("searchBookISBN");
            List<Book> bookList = BookDB.advSearchBook(bookTitle, bookAuthor, bookGenre, bookISBN);
            if (bookList.isEmpty()) {
                System.out.println("admin.BookManager.doPost()" + "Book Does not exist");
                cookies = request.getCookies();

                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("userCookie")) {
                        url = "/home.jsp";
                        user = UserDB.selectUser(cookie.getValue());
                        request.setAttribute("user", user);
                    } else {
                        url = "/guestHome.jsp";
                    }
                }

                message = "Book does not exist with this title or author.";
                request.setAttribute("searchErrorMessage", message);
                List<Book> books = BookDB.selectAllBooks();
                request.setAttribute("books", books);
                request.setAttribute("user", user);
                request.getServletContext().getRequestDispatcher(url).forward(request, response);
            } else {
                url = "/viewSearchResult.jsp";
                request.setAttribute("bookResult", bookList);
                request.setAttribute("user", user);
                getServletContext().getRequestDispatcher(url).forward(request, response);
            }
        }
    }
}
