The purpose of the site is to determine if text entered by the user has been taken from any documents stores in the database (for demo purposes, the database consisted of Shakespeare or the Adventures of Sherlock Holmes.)

I created and deployed this website using .NET MVC Core, using MVC or model-view-controller site architecture. The website consists of an MVC layer that manages interacitons with the user, and a business logic layer that retrieves documents from a database server and compares them to the user-entered text.

The website is implemented using mainly C# and .cshtml 'razor page' files, which combine both C# and html to generate new and unique webpages on demand. 

For example, MVC/Controllers/HomeController is the logical core of the website's interactive layer. This file handles user requests for new and original results pages by querrying the business layer to perform computation, then pushing the results of that computation to 'views' which consist of .cshtml files.   

One important view file is views/home/results.cshtml which generates a new unique results page given a collection of 'result' objects.  

The website was originally hosted via Azure web services and an Azure SQL database sever. It is currently offline to avoid maintenance costs.

These folders contain just files in which I wrote code. .Net project files, Dlls, caches and other site files not included. 