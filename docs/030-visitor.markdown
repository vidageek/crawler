---
permalink: visitor.html
title: Visitor
layout: doc
---

All Visitors are runned on a multi-thread environment, so they **MUST** be Thread Safe. Don't know
if your visitor is thread safe? Send us an email at the user list. 

## ContentVisitor

The only code you'll usually need to write is a implementation of `net.vidageek.crawler.ContentVisitor` .
This interface provides two methods:

- `void visit(Page page)` : This method is called on each page found on the site.
- `void onError(Url errorUrl, Status statusError)` : This method is called on pages that failed to respond with a `Status.OK` or `Status.REDIRECTION` for any reason.

## PageVisitor

`PageVisitor` is a sub interface of `ContentVisitor`. Usually, you'll won't need to implement this since you 
can use an already implemented `PageVisitor`.

- `DoesNotFollowVisitedUrlVisitor`: Using this visitor you'll only visit each url once.
- `DomainVisitor`: This visitor forces crawler to not go outside the site domain.
- `RejectAtDepthVisitor`: Basically, you can consider the start page as depth=0, all pages linked from the start 
as depth=1 and so on (yes, BFS). So you can configure how deep on the site the crawler will go. 

Did I mention that you can compose these PageVisitors?
