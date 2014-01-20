'use strict';

angular.module('yoApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/medialist', {
        templateUrl: 'views/medialist.html',
        controller: 'MedialistCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
