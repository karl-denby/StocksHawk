# StocksHawk

This is developed from the starter code for project 3 in Udacity's [Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801).

Nanodegree students are evaluated against the project [rubric](https://review.udacity.com/#!/rubrics/140/view)


# Required changed

* RTL text for Arabic must be supported properly [Done]
* Must not crash when adding a new stock [Done]
* Must show a detail screen for stocks [Done]
* Must create a widget to show stocks [Done]
* Widget auto refreshes with content provider [In Progress]
* Tablet layout [In Progress]


# Screenshots
Main Screen - with FAB (Floating action button)

![PhoneActivity](https://github.com/karl-denby/StocksHawk/raw/master/screenshots/00_main.png)

Detail Screen - Loading

![DetailActivity](https://github.com/karl-denby/StocksHawk/raw/master/screenshots/01_detail.png)

Detail Screen - with graph

![DetailActivity](https://github.com/karl-denby/StocksHawk/raw/master/screenshots/02_detail.png)

Add a Stock

![AddStockFragment](https://github.com/karl-denby/StocksHawk/raw/master/screenshots/03_add_stock.png)

Widget

![Widget](https://github.com/karl-denby/StocksHawk/raw/master/screenshots/widget_preview.png)


# Learnings

* Creating a widget that would auto update with data from a content provider
* Problems with upstream library/API (Yahoo Finance) can cause major issues downstream, these included crashes and empty history data
* ConstraintLayout is quite a nice way to do things, but requires some clunky wrestling with the UI/Text editor


# Contributing

Pull requests gratefully accepted.
