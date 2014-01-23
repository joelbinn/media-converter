'use strict';

angular.module('yoApp')
  .controller('MainCtrl', function ($scope, $http) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
    $scope.pingBe = function () {
      $http.get('/mconvrest/ping').success(function (data) {
        $scope.beresponse = data;
      });
    };
  });
