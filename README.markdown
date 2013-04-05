Essentials Development Readme - 3.0
=============================

The official repository is at:
https://github.com/essentials/Essentials

The master repository is not for production use. Use branch 2.9 for production use code.

We use NetBeans 7 for development.

Recommended NetBeans plugins:

* Git
* PMD & FindBugs ( http://kenai.com/projects/sqe/pages/Home )

Please follow the format guidelines that are saved in the project properties.

Windows users, please read this: http://help.github.com/line-endings/
The default line ending is LF.

To build all jars, open this folder and use `mvn clean install` to build it. You'll find all jars inside the jars/ folder.

If you create pull requests, always make them for the master branch.

The essentials bug tracker can be found at http://www.assembla.com/spaces/essentials/tickets

If you want to build the new messages files, you require the gettext command line utils (xgettext, msgcat, msgmerge).

For Linux / Mac install them using your package manager (e.g. MacPorts). For Windows install using the installer from http://sourceforge.net/projects/gnuwin32/files/gettext/0.14.4/gettext-0.14.4.exe/download or get the utils from http://sourceforge.net/projects/cppcms/files/boost_locale/gettext_for_windows/gettext-tools-static-0.18.1.1.zip/download and put them on your path. 
