# Simple-search-engine-with-Hadoop
This is a simple search engine. First, use the crawler in JavaSpider3 to crawl some web pages. Then you can use IKAnalyzer in the same program to split the contents into single words.  After that, use Hadoop to build a inverted sorting list. Finally, get an input from a web page and search it in your list. Return the results if you find any.
The demo is attached in this project. Notice that the in the directory 'web', each page is stored in a single file with a unique number, which is derived from the url. And the inverted sorting list is stored the file 'file'.
If you have installed Xampp in your laptop, put all the files under directory \opt\lampp\htdocs and then open index.html in directory 'html'. You can see the web page of my search engine.
