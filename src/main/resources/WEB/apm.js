
$(function () {

  function rest(type, url, data) {
    if('' == url){
      return;
    }
    $('#div0').text('');
    $('#pre1').text('');
    $.ajax({
      type: type,
      url: url,
      data: data,
      contentType: 'application/json', 
      cache: false,
      dataType : "text",
      success: function (data, textStatus) {
        //console.log(data);
        $('#div0').text(textStatus.toUpperCase());
        var str = data;
        try {
            str = JSON.stringify(JSON.parse(data), null, 2);
        } catch (error) {
        }
        $('#pre1').text(str);
      },
      error: function (xhr, textStatus, errorThrown) {
        $('#div0').text(textStatus.toUpperCase() + ' - ' + errorThrown);
      }
    });
  }

  $('#button_get').click(function () {
    path = $('#path').val();
    rest("get", path, null);
  });

  $('#button_post').click(function () {
    path = $('#path').val();
    body = $('#input_body').val();
    rest("post", path, body);
  });

  $('#button_clear').click(function () {
    $('#div0').text('');
    $('#pre1').text('');
  });


  $('.dropdown-item').click(function () {
    console.log(this);
    $('#path').val($(this).text());
  });

/*
  $('#button_health').click(function () {
    $('#path').val($('#button_health').text());
  });
*/

});