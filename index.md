---
layout: default
---


[**AdminFaces**](https://github.com/adminfaces) is an open source project which integrates [Primefaces](http://primefaces.org/), [Bootstrap](http://getbootstrap.com/) and [Admin LTE](https://almsaeedstudio.com/themes/AdminLTE/index2.html/) in order to create fully responsive and mobile ready [JSF](https://javaserverfaces.java.net/) applications.


AdminFaces is composed by the following projects:

* [Admin Theme](http://github.com/adminfaces/admin-theme): A [Primefaces theme](http://primefaces.org/themes) based on Bootstrap and Admin LTE where Primefaces components are customized to look like and work with mentioned frameworks.
* [Admin Template](http://github.com/adminfaces/admin-template): It's a fully responsive [Java Server Faces](https://javaserverfaces.java.net/) admin template which is also based on Bootstrap and Admin LTE.
* [Admin Showcase](http://github.com/adminfaces/admin-showcase): A web application which demonstrates AdminFaces main features and components.
* [Admin Designer](http://github.com/adminfaces/admin-designer): It's the same showcase application with Admin Theme and Admin Template bundled (instead of being library dependencies) in order to make it easier to customize the theme and the template.
* [Admin Starter](http://github.com/adminfaces/admin-starter): A simple starter project to get you started with AdminFaces.
* [Admin Mobile](http://github.com/adminfaces/admin-mobile): Admin showcase mobile application using Android webview.

#### Documentation

Current version (under development) of documentation can be [found here]({{site.baseurl}}/latest/index.html).

Following is documentation for each released version:

{% for release in site.github.releases %}
  * [{{ release.tag_name }}]({{site.baseurl}}/{{ release.tag_name }}/index.html) ([PDF]({{site.baseurl}}/{{ release.tag_name }}/index.pdf))
{% endfor %}

#### Credits
* Maintained by:
  * [Rafael M. Pestano](https://github.com/rmpestano) and community

[rmpestano]: https://github.com/rmpestano
