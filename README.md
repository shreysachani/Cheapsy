# Car Rental Price Analysis

The **Car Rental Price Analysis** project is designed to help users find the best rental car deals by scraping data from popular car rental websites based on user search inputs. This tool gathers and stores HTML data from selected sites, then parses and analyzes the content to present an efficient comparison of rental options. The project also incorporates various features like frequency analysis, spell-check, and search optimization to enhance user experience and provide insights into pricing trends.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
  - [Web Crawler](#1-web-crawler)
  - [HTML Parser](#2-html-parser)
  - [Frequency Count](#3-frequency-count)
  - [Spell Checking](#4-spell-checking)
  - [Search Frequency](#5-search-frequency)
  - [Word Completion](#6-word-completion)
  - [Inverted Indexing](#7-inverted-indexing)
  - [Page Ranking](#8-page-ranking)
  - [Data Validation using Regex](#9-data-validation-using-regex)
  - [Finding Patterns using Regex](#10-finding-patterns-using-regex)

## Introduction
The **Car Rental Price Analysis** project explores rental car pricing trends by scraping data from the following websites:
- [Avis](https://www.avis.ca/en/home)
- [Budget](https://www.budget.ca/en/home)
- [Car Rental](https://www.carrentals.com/)

The project uses user-provided search inputs to scrape these websites and store the content in respective HTML files. This data is later parsed and compared to display results back to the user. This approach allows users to make better-informed rental choices based on live data from major car rental providers.

## Features

### 1. Web Crawler
The web crawler scrapes rental car data from specified websites based on user input, gathering essential details such as vehicle type, rental price, and other useful information. This data is stored in HTML files for subsequent trend analysis and price comparison.

### 2. HTML Parser
The HTML parser extracts specific data from stored HTML files and saves it for further analysis. This enables structured data comparison across different websites.

### 3. Frequency Count
The frequency count feature calculates how often specific terms appear, which can reveal popular rental options or frequent price points.

### 4. Spell Checking
This feature corrects spelling errors in text inputs by cross-referencing them with a designated dictionary.

### 5. Search Frequency
Tracks the frequency of user searches for specific rental terms, providing insight into popular search trends.

### 6. Word Completion
The word completion feature predicts the remainder of a term as users type, enhancing the search experience.

### 7. Inverted Indexing
Stores key rental terms and associates them with their locations within the dataset, allowing rapid search functionality.

### 8. Page Ranking
Ranks rental cars based on occurrences across web pages, with higher-ranking cars appearing more frequently in search results.

### 9. Data Validation using Regex
Validates user input against pre-set regular expression patterns to ensure data accuracy.

### 10. Finding Patterns using Regex
During data operations, regex is used to identify specific data patterns, ensuring that only relevant information is extracted.

---

This project provides a powerful tool for comparing car rental prices across platforms, helping users make well-informed decisions based on comprehensive data analysis.
