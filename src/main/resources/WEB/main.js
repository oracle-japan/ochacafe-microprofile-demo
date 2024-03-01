
/*
window.addEventListener('load', function(){})；
document.addEventListener('DOMContentLoaded', function(e){});
*/

var path = document.getElementById('path');
var body = document.getElementById('input_body');
var div0 = document.getElementById('div0');
var pre1 = document.getElementById('pre1');

function rest(type, url, data) {
  if(null == url || '' == url){
    return;
  }
  div0.innerText = 'Sending request ...'
  pre1.innerText = ''
  
  axios({
    method: type,
    url: url,
    headers: {
      'Content-Type' : 'application/json'
    },
    responseType: 'text',
    data: data
  })
    .then(function (response) {
      div0.innerText = response.statusText.toUpperCase();
      var str = response.data;
      try {
        str = JSON.stringify(JSON.parse(response.data), null, 2);
      } catch (error) {
      }
      pre1.innerText = str;
    })
    .catch(function (error) {
      div0.innerText = error.response.status + ' - ' + error.response.statusText;
    });
}

document.getElementById('button_get').addEventListener("click", function (event) {
  rest('get', path.value, null);
});

document.getElementById('button_post').addEventListener("click", function (event) {
  rest('post', path.value, body.value);
});

document.getElementById('button_clear').addEventListener("click", function (event) {
  div0.innerText = ''
  pre1.innerText = ''
});

var ddItems = document.getElementsByClassName("dropdown-item");
for (var i=0, len=ddItems.length|0; i<len; i=i+1|0) {
  ddItems[i].addEventListener("click", function (event) {
    path.value = event.target.innerText;
  });
}
