# GCC Course Signup

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

This is a simple desktop application, written by an alumni ✌, designed to help Grove City College students automatically register for courses as soon as course registration becomes available.

This project has no affiliation with Grove City College in any way.

## Features

- [x] GUI to specify registration time (synced to your timezone)
- [x] Automatically "submits" courses in the Add/Drop form at a specified time
- [x] Supports multiple browser instances at once (ideal for adding 6+ courses)
- [x] Keeps you signed in indefinitely while running (allowing you to setup well in advance)
- [x] Automatic web driver management to work with installed browser's version
- [ ] Supports different browser types (currently only Chrome)

## Instructions

1. Start the application
2. Specify what time course registration occurs for you
3. Click the `Launch` button to create a new browser instance
4. Log in to myGCC once the browser finishes loading
   1. Never enter your credentials outside the real myGCC website!
   2. Make sure the browser navigated to the real website before attempting to log in.
5. Once logged in, the browser will automatically navigate to your course registration page
6. Specify which course you'd like to sign up for on the course signup page. If you need to sign up for more than six courses, then click the `Launch` button again to create a second browser instance.
   1. Once you finish specifying your courses, make sure the `Add Course(s)` button is visible otherwise the application will not be able to click it!
7. Make sure your computer stays awake until it is time for course registration
8. ???
9. Profit

## Notes

- The application is literally clicking the buttons within the browser. 
  If you cannot see the button, the application cannot see it either.
  This means you cannot navigate away from myGCC or open up new tabs within the browser window the application is controlling.
- Changing the time course registration occurs will not update existing browser instances. You will need to close and re-launch the browsers to use the new time.
- Closing the application will quit all open browser instances controlled by the application.
- Once course registration is complete, the application will pause and wait for you to close the browsers. 
  This lets you view the results of the registration or make changes if registration failed without having to reload the page.

## User Interface

<p align="middle">
  <img src="/images/light_mode_gui.png" width="400" />
  <img src="/images/dark_mode_gui.png" width="400" /> 
</p>

## License

    Copyright 2022 Christian Burns

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
