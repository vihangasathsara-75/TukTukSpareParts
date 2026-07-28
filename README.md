# TukTukSpareParts



A JavaFX desktop application for managing the inventory and operations of the Malabe Tuk-Tuk \& Three-Wheeler Spares Depot.



Built for CM1601 Programming Fundamentals — Coursework 2  

RGU / IIT Academic Year 2025-26



---



## About the Project



The depot currently stores its inventory and dealer data in legacy text 

files that are inconsistently formatted. This application reads those 

files, cleans the data automatically, and provides a graphical interface 

for day-to-day depot operations.



---



## Features


\- Loads and parses dirty legacy data from text files on startup

\- Displays all spare parts grouped by category and sorted by part code

\- Add, edit, and delete spare parts with full input validation

\- Multi-criteria search using category, keyword, and price range

\- Randomly selects 4 unique dealers sorted alphabetically by location

\- Point of sale cart with bulk and synergy discount calculation

\- Per-part low stock threshold with live warning panel

\- Audit log that records every data-modifying action with a timestamp



---



## Requirements



| Requirement | Version |

|---|---|

| JDK | 21 |

| JavaFX | 21.0.6 (downloaded automatically via Maven) |

| Maven | Included via mvnw wrapper |

| JUnit | 5.12.1 (downloaded automatically via Maven) |



---



## How to Run



### Option 1 — Run from VS Code or IntelliJ



1\. Open the `TukTukSpareParts` folder in your IDE

2\. Make sure JDK 21 is selected as the project SDK

3\. Run `Launcher.java` or `HelloApplication.java`



### Option 2 — Run from the terminal

1. Open your terminal in the project root directory (`TukTukSpareParts/`).
2. Build and run the application using the included Maven wrapper:

   **On Windows:**
   ```cmd
   .\mvnw clean javafx:run

