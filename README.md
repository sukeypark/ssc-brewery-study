## View layout
navbar가 있는 layout 생성하기
### fragment
fragment를 생성하여 다른 html에서 사용할 수 있다.

#### fragment 생성

```
<!-- fragments/footer.html -->
<!DOCTYPE html>
<html>
<head></head>
<body>
  <div th:fragment="footer">footer</div>
</body>
</html>
```
#### 요소 사용
* insert 또는 replace를 사용하여 요소를 대체 또는 삽입한다.

```
<!-- index.html -->
<!DOCTYPE html>
<html>
<head></head>
<body>
  <div th:replace="fragments/footer :: footer"></div>
</body>
</html>
```

### parameterized fragment

#### fragment 생성

```
<!-- fragments/footer.html -->
<!DOCTYPE html>
<html>
<head></head>
<body>
  <div th:fragment="footer (message)">
  	<span th:text="${message}">message</span>
  </div>
</body>
</html>
```
#### 요소 사용
* insert 또는 replace를 사용하여 요소를 대체 또는 삽입한다.

```
<!-- index.html -->
<!DOCTYPE html>
<html>
<head></head>
<body>
  <div th:replace="fragments/footer :: footer('footer')"></div>
</body>
</html>
```

### bootstrap navbar
- nav: 메뉴형태로 꾸며줌
- navbar: 수평메뉴로 꾸며줌
- navbar-default / navbar-inverse: 기본색상 / 반전색상
- navbar-collapse: 화면크기가 작아지면 메뉴를 collapse하고 navbar-toggle을 보여준다.

```
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" th:href="@{/}"><span></span></a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#main-navbar">
                <span class="sr-only"><os-p>Toggle navigation</os-p></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="navbar-collapse collapse" id="main-navbar">
            <ul class="nav navbar-nav navbar-right">

                <li th:fragment="menuItem (path,active,title,glyph,text)" class="active" th:class="${active==menu ? 'active' : ''}">
                    <a th:href="@{__${path}__}" th:title="${title}">
                        <span th:class="'glyphicon  glyphicon-'+${glyph}" class="glyphicon glyphicon-home" aria-hidden="true"></span>
                        <span th:text="${text}">Template</span>
                    </a>
                </li>

                <li th:replace="::menuItem ('/','home','home page','home','Home')">
                    <span class="glyphicon glyphicon-home" aria-hidden="true"></span>
                    <span>Home</span>
                </li>

                <li th:replace="::menuItem ('/customers/find','customers','find customers','search','Customers')">
                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    <span>Find Customer</span>
                </li>

                <li th:replace="::menuItem ('/beers/find','beers','find beers','search','Find beers')">
                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    <span>Find Beer</span>
                </li>

                <li th:replace="::menuItem ('/brewery/breweries','breweries','breweries','th-list','Breweries')">
                    <span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
                    <span>Breweries</span>
                </li>

            </ul>
        </div>
    </div>
</nav>
```
### layout으로 덮어씌우기
각 페이지에서는 layout을 불러와서 페이지에 해당하는 body를 작성한 후 layout의 replace 부분에 parameter로 전달할 수 있다.
```
<!doctype html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="fragments/layout :: layout (~{::body}, 'customers')">
<head>
</head>
<body>
  index
</body>
</html>
```