# **Library Management System**
The focus of this software will be to manage all important steps of a Library. The software should keep data about all  the books in the library, such as ISBN of the book, title, category of the book, supplier, purchased date, purchased price, original price, selling price, author, stock (an image of book cover is a bonus). At the same time, we should keep track of the sold products by bill number (or Order Id), their sold quantities, prices, and date of transaction. 

The application will have a three-level user system: Librarian, Manager and Administrator. Each will have different  views and usage of the software. Note that each of the users has a username and a password to enter in the software  (obviously a role as well). 

Librarian -> Has the right to check out books that a customer may need from the bookstore. This means that the  librarian should create a bill and enter the data of the bought books, such as ISBN of the book and its quantity. If the  book is out of stock or does not exist, give an alert to the librarian. The software should provide him the total amount  of the bill and it should be in printable format ([BillNo].txt). The updates in the software file should be done  automatically by adding the data into the respective files. 

Manager -> The manager has the right to supply the bookstore with the needed books. So, he/she can create new book category, authors, and/or add books of the same category to the stock of the bookstore. The manager should  be informed when entered in the system if there are less than 5 items of a book in the stock.  The same user may also check the performance of the librarians by checking their total number of bills, books sold, and the total amount of money made for a certain date or between a certain period of time. Also, the statistics about the books sold and bought should be provided to them whenever requested with daily, monthly and/or total filters. 

Administrator -> The administrator has the right to manage almost everything that Librarian and Manager does. Beside the above permissions, the admin has the right to manage the employees (Librarian and Manager), by registering, modifying, and deleting them. The data kept about employees include: name, birthday, phone, email, salary, access level, and other information about them. The software should also provide to the admin data about total incomes (total  of books sold) and total cost (total of items bought and staff salaries) during a period.

Another important role of the administrator is to revoke permissions from librarians or managers. This means that the permissions should be role-based. (Assume that the admin decides that librarians can add new books to the stock beside managers).


## Setup
This project is built using maven. Maven should be loaded automatically when you open the project.
It includes dependencies like javaFX(gui), jUnit and mockito(testing). JavaFx version should be 21 or above and Java SDK 17 or above.


### **Sample data for testing (for login)**

Librarian One ->
librarian1password

Manager One ->
manager1password

Administrator One ->
admin1

### CONTRIBUTORS
*Adi Salaj* 

*Arlind Alliu*

*Denaldo Selala*

