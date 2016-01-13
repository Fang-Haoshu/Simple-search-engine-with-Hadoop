# Simple-search-engine-with-Hadoop
This is a  project of Distributed Computing course in SJTU. It consists of four parts:
>Crawler

>Word breaker

>Inverted sorting list

>Home page

###How it works
First, the crawler in folder JavaSpider3 will crawl some web pages. 

Then the word breaker IKAnalyzer in the same program will split the contents into single words.  

After that, use Hadoop to build a inverted sorting list. 

Finally, get an input from a web page and search it in your list. Return the results if you find any.
###Demo
The demo is attached in this project. In the directory 'web', each page is stored in a single file with a unique number, which is derived from the url. And the inverted sorting list is stored the file 'file'.

If you have installed Xampp in your laptop, put all the files under directory \opt\lampp\htdocs and then open index.html in directory 'html'. You can see the web page of my search engine.
