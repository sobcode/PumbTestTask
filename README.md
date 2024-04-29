**PumbTestTask**


Environment: Java 17, SpringBoot, MySQL


**Animal Data Management API**

This repository contains the code for a REST API that allows users to manage animals data. Users can upload animals data from CSV or XML files and search for animals based on various criteria.


**API Documentation**

The API is documented using Swagger. You can access the interactive documentation at http://localhost:8081/swagger-ui/index.html.


**Endpoints**:

POST /api/files/uploads (+ CSV or XML file as a request body) - Uploads animal data from a CSV or XML file.

GET /api/animals/search - Searches for animals based on provided criteria (type, category, sex) and pagination options.

  ex.: .../api/animals/search?type=cat&page=0&size=5
  
       .../api/animals/search?category=3&sex=female
