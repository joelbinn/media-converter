'use strict';

angular.module('yoApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'ngTable'
  ])
  .config(function ($routeProvider, $httpProvider) {
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

    $httpProvider.interceptors.push(function () {
      return {
        'request': function (config) {
          //console.log('Request config: %o', config);
          return config;
        }
      };
    });
  });
