需要在bootstrap.min.css和select2.min.css文件末尾添加下面的样式
并且修改select2.min.css 中的

>**  .select2-container .select2-selection--single>height: 34px;
>**  .select2-container--default .select2-selection--single .select2-selection__rendered>line-height: 34px;

```
.form-control .select2-choice {
    border: 1px solid #e5e5e5;
    background-color: #fff;
    background-image: none;
    filter: none;
    height: 34px;
    padding: 3px 0 0px 12px;
}


.select2-container.select2-drop-above .select2-choice {
    border-bottom-color: #e5e5e5;
    background-color: #fff;
    background-image: none;
    filter: none;
}


.select2-drop {
    border: 1px solid #e5e5e5;
    background-color: #fff;
    background-image: none;
    -webkit-box-shadow: none;
    box-shadow: none;
    filter: none;
    border-top: 0;
}
.select2-drop-auto-width {
    border-top: 1px solid #e5e5e5;
}


.select2-drop.select2-drop-above {
    border-top: 1px solid #e5e5e5;
    -webkit-box-shadow: none;
    box-shadow: none;
}


.select2-drop-active {
    border: 1px solid #999;
    border-top: 0;
}


.select2-container .select2-choice .select2-arrow {
    background-image: none;
    background-color: #fff;
    filter: none;
    border-left: 1px solid #e5e5e5;
}


.select2-container.select2-container-active .select2-arrow,
.select2-container.select2-dropdown-open .select2-arrow {
    border-left: 0 !important;
}


.select2-container .select2-choice .select2-arrow b {
    background-position: 0 1px;
}


.select2-search input {
    border: 1px solid #e5e5e5;
    background-color: #fff !important;
    filter: none;
    margin: 0;
    outline: 0;
    border: 1px solid #e5e5e5;
    webkit-appearance: none !important;
    color: #333333;
    outline: 0;
    box-shadow: none;
    height: auto !important;
    min-height: 26px;
    padding: 6px 6px !important;
    line-height: 20px;
    font-size: 14px;
    font-weight: normal;
    vertical-align: top;
    background-color: #ffffff;
    -webkit-box-shadow: none;
    box-shadow: none;
    margin-top: 5px;
}


.form-control.select2-container {
    border: 0;
    height: auto !important;
    padding: 0px;
}


.select2-container-active .select2-choice,
.select2-container-active .select2-choices {
    border: 1px solid #999 !important;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}


.select2-dropdown-open .select2-choice {
    border-bottom: 0 !important;
    background-image: none;
    background-color: #fff;
    filter: none;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}


.select2-dropdown-open.select2-drop-above .select2-choice,
.select2-dropdown-open.select2-drop-above .select2-choices {
    border: 1px solid #999 !important;
    border-top: 0 !important;
    background-image: none;
    background-color: #fff;
    filter: none;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}


.select2-drop.select2-drop-above.select2-drop-active {
    border: 1px solid #999 !important;
    border-bottom: 0 !important;
}


.select2-dropdown-open .select2-choice .select2-arrow b {
    background-position: -18px 1px;
}


.select2-results {
    margin: 5px 0;
}


.select2-results .select2-highlighted {
    background: #eee;
    color: #333;
}


.select2-results li em {
    background: #feffde;
    font-style: normal;
}


.select2-results .select2-highlighted em {
    background: transparent;
}


.select2-results .select2-highlighted ul {
    background: #fff;
    color: #000;
}


.select2-results .select2-no-results,
.select2-results .select2-searching,
.select2-results .select2-selection-limit {
    padding: 3px 7px 4px;
    background: #f4f4f4;
    display: list-item;
}


.select2-container-multi {
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -ms-border-radius: 4px;
    -o-border-radius: 4px;
    border-radius: 4px;
}
.select2-container-multi .select2-choices {
    -webkit-border-radius: 4px;
    -moz-border-radius: 4px;
    -ms-border-radius: 4px;
    -o-border-radius: 4px;
    border-radius: 4px;
}
.select2-container-multi.select2-dropdown-open {
    -webkit-border-radius: 4px 4px 0 0;
    -moz-border-radius: 4px 4px 0 0;
    -ms-border-radius: 4px 4px 0 0;
    -o-border-radius: 4px 4px 0 0;
    border-radius: 4px 4px 0 0;
}
.select2-container-multi.select2-dropdown-open .select2-choices {
    -webkit-border-radius: 4px 4px 0 0;
    -moz-border-radius: 4px 4px 0 0;
    -ms-border-radius: 4px 4px 0 0;
    -o-border-radius: 4px 4px 0 0;
    border-radius: 4px 4px 0 0;
}
.select2-container-multi.select2-dropdown-open.select2-drop-above {
    -webkit-border-radius: 0 0 4px 4px;
    -moz-border-radius: 0 0 4px 4px;
    -ms-border-radius: 0 0 4px 4px;
    -o-border-radius: 0 0 4px 4px;
    border-radius: 0 0 4px 4px;
}
.select2-container-multi.select2-dropdown-open.select2-drop-above .select2-choices {
    -webkit-border-radius: 0 0 4px 4px;
    -moz-border-radius: 0 0 4px 4px;
    -ms-border-radius: 0 0 4px 4px;
    -o-border-radius: 0 0 4px 4px;
    border-radius: 0 0 4px 4px;
}


.select2-container-multi .select2-choices {
    padding-left: 6px;
    min-height: 34px;
    border: 1px solid #e5e5e5;
    background-image: none;
    background-color: #fff;
    filter: none;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}


.select2-container-multi.select2-container-active .select2-choices {
    border: 1px solid #999 !important;
    background-image: none;
    background-color: #fff;
    filter: none;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}


.select2-container-multi .select2-choices .select2-search-choice {
    padding: 3px 5px 3px 18px;
    margin: 5px 0 3px 5px;
    border: 1px solid #e5e5e5;
    background-image: none;
    background-color: #fff;
    filter: none;
    -webkit-box-shadow: none !important;
    box-shadow: none !important;
}
.select2-container .select2-choice {
    height: 34px;
    line-height: 34px;
}
.select2-container--default .select2-selection--single .select2-selection__rendered{
    color: #444;
    line-height: 34px;
}
.select2-container .select2-selection--single {
    box-sizing: border-box;
    cursor: pointer;
    display: block;
    height: 34px; 
    user-select: none;
    -webkit-user-select: none;
}
```
