---
permalink: index.html
title: Crawler
---

Crawler is a simple Java web crawler/spider/joe or any other name you want to call it.

The main goal is to abstract that boring and error-prone code from your codebase and let you 
focus on crawling the site. Its quite easy to use it:

    CrawlerConfiguration cfg = new CrawlerConfiguration("http://www.yoursite.com");
    PageCrawler crawler = new PageCrawler(cfg);
    
    crawler.crawl(new YourPageVisitor());

You don't even need to know that there is code to make parallel requests, handle content encoding,
find links on the pages, normalize those links and so on.

You just focus on implementing your `net.vidageek.crawler.PageVisitor` . Actually, usually you'll only implement a
`net.vidageek.crawler.ContentVisitor`, since you'll use some composable `PageVisitor`s already provided.

Ah, almost forgot! Yes. Crawler will follow redirects by default.

Happy Crawling! 
