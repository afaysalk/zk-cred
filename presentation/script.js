

const hideDivLink = document.getElementById('hide-div-link');
  hideDivLink.addEventListener('click', function() {
    window.opener.document.querySelector('#div-to-hide').style.display = 'none';
  });