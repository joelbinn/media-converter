'use strict';

describe('Controller: MedialistCtrl', function () {

  // load the controller's module
  beforeEach(module('yoApp'));

  var MedialistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MedialistCtrl = $controller('MedialistCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.imdbItems.length).toBe(0);
  });
});
