Feature: Flowchart using general shapes in draw.io
  Start > Read A > Read B > Calculate C + A = B > Print C > End
  Test scripts should support ability to add/update/delete steps in the flowchart
  Test scripts should support ability to add/change shapes
  Test scripts should support ability to add/update/delete text in the shapes
  Test script should support negative test cases such as invalid input

  Background: Open new drawing
    Given the user has a flowchart in progress

  Scenario: create a complete flowchart
    Then all shapes and steps should be reflected

  Scenario Outline: Add, update and delete a step in flowchart
    When the user adds a new <step> adjacent <existing step>
    Then the <step> should be added to the appropriate point
    And the user should be able to update the <step> with <update step>
    And the user should be able to delete the <step> with <update step>

    Examples:
      | step     | existing step       | update step |
      | Read All | Calculate C = A + B | Read W      |

  Scenario Outline: Add, update and delete a shape in flowchart
    When the user adds a new <shape> with <step>
    Then the <step> should be added to the appropriate point
    And the user should have an option to update the <update_shape> with <step>
    And the user should have an option to delete the <update_shape> with <step>

    Examples:
      | shape     | step   | update_shape |
      | Rectangle | Read C | Ellipse      |

  Scenario Outline: Add, update and delete a text in flowchart
    When the user adds a new <shape> with <step>
    Then the <step> should be added to the appropriate point
    And user should be able to update the text <step> with <step_description>
    And user should be able to delete the text <step_description>

    Examples:
      | shape     | step   | step_description |
      | Rectangle | Read C | Read CL          |

    Scenario: Negative case for invalid geometry
      When the user updates a shape with invalid geometry
      Then the value should auto calibrate to NaN






