Feature: Calculator Mobile tests

  Background: #<Describe Here a PreCondition for ff scenarios>

  @native @Add
  Scenario Outline: Addition operation with 2x2 numbers
    Given User opens "nativeCalculator" App
    And User click on the agree button
    And User click on the "<number11>" button
    And User click on the "<number12>" button
    And User click on the add button
    And User click on the "<number21>" button
    And User click on the "<number22>" button
    And User click on the equal button
    Then User validates that sum is "<sum>"

    Examples: 
      | number11 | number12 | number21 | number22 | sum |
      |        1 |        2 |        3 |        4 |  46 |

  @web
  Scenario: Addition operation with 2x2 numbers
    Given User opens "webChrome" App
    And User click on the login link
    And User enter the username as "ortoni"
    And User enter the password as "Pass1234"
    When User click on the login button
    Then Login should be success
