'use strict';

angular.module('yoApp')
  .controller('MedialistCtrl', function ($scope, $http) {
    $scope.movies = [
      {
        file: '/media/RASPBMC/aaa.avi',
        title: 'Banan',
        imdbId: 'tt012343',
        image: 'http://i.media-imdb.com/images/mobile/film-40x54.png',
        id: 1
      },
      {
        file: '/media/RASPBMC/bbb.avi',
        title: 'The Ring',
        imdbId: 'tt009090',
        image: 'http://i.media-imdb.com/images/mobile/film-40x54.png',
        id: 2
      },
      {
        file: '/media/RASPBMC/ccc.avi',
        title: 'Scarface',
        imdbId: 'tt1111',
        image: 'http://i.media-imdb.com/images/mobile/film-40x54.png',
        id: 3
      }
    ];
    $scope.selected = {name: '/media/RASPBMC/xyz.avi', imdbId: 'tt012343'};
    $scope.imdbItems = [];
    $scope.searchTerm = '';
    $scope.changeUrl = function (imageUrl) {
      if (imageUrl) {
        return imageUrl.replace(/http:\/\/ia.media-imdb.com\/images\/(.*)/, '/mconvrest/imdb/image/$1');
      } else {
        return '/images/film-40x54.png';
      }
    };
    $scope.$watch('searchTerm', function () {
      if ($scope.searchTerm && $scope.searchTerm.length > 0) {
        $http.get('/mconvrest/imdb/suggests/' + $scope.searchTerm[0] + '/' + $scope.searchTerm.replace(' ', '_') + '.json')
          .success(function (data) {
            console.log('>>> Returned Data: %o', data);
            var match = data.match(/imdb\$[a-zA-Z0-9]*\((.*)\)$/);
            if (match && match.length === 2) {
              var obj = angular.fromJson(match[1]);
              console.log('Object %o', obj);
              $scope.imdbItems = obj.d.filter(function (e) {
                return e.id.indexOf('tt') === 0;
              });
            }
          })
          .error(function (data) {
            console.log('ERROR; Data: %o', data);
          });
      } else {
        $scope.imdbItems = [];
      }
    });

    $scope.search = function(movie) {
      $scope.selectedMovie = movie;
    };

    $scope.selectImdbItem = function(imdbItem) {
      $scope.selectedImdbItem = imdbItem;
      console.log('Movie: %o', $scope.selectedImdbItem);
    };

    $scope.saveImdbItemToMovie = function() {
      $scope.selectedMovie.title = $scope.selectedImdbItem.l;
      $scope.selectedMovie.image = $scope.selectedImdbItem.i[0];
      $scope.selectedMovie.imdbId = $scope.selectedImdbItem.id;
      $http.put('/mconvrest/movies/'+ $scope.selectedMovie.id,  $scope.selectedMovie)
        .success(function(){
        console.log('Saved: %o',  $scope.selectedMovie);
      });
      $scope.selectedImdbItem = undefined;
      $scope.selectedMovie = undefined;
      console.log('Movie: %o', $scope.selectedMovie);
    };

    $scope.abort = function() {
      $scope.selectedImdbItem = undefined;
      $scope.selectedMovie = undefined;
      $scope.searchTerm = undefined;
    };
    $http.get('/mconvrest/movies')
      .success(function(movies) {
        $scope.movies = movies;
      });
  });
