# Markdown for Netbeans ![Description Here](https://raw.githubusercontent.com/moacirrf/netbeans-markdown/main/images/nblogo48x48.png)

***

## Description
This plugin adds some additional features to Apache Netbeans Markdown Editor.
- Preview
- Split Window
- Suggestion
- Export to DOCX, PDF and HTML

## Tables

| Header 1 | Header 2 |  Header 3 |
|----------|----------|-----------|
|   Col 1  |   Col 2  |   Col 3   |

## Checkboxes

- [x] Option 1
- [ ] Option 2

```java

    private static ParamValue valueFromConstraintInstance(Constraint<?> constraint) {
        return switch (constraint) {
            case AtLeast<?> atLeast -> new ParamValue(Condition.GREATER_EQUALS_THAN, valueFromExpression(atLeast.bound()), false);
            case jakarta.data.constraint.AtMost<?> atMost -> new ParamValue(Condition.LESSER_EQUALS_THAN, valueFromExpression(atMost.bound()), false);
            case jakarta.data.constraint.GreaterThan<?> greaterThan ->
                    new ParamValue(Condition.GREATER_THAN, valueFromExpression(greaterThan.bound()), false);
            case jakarta.data.constraint.LessThan<?> lessThan -> new ParamValue(Condition.LESSER_THAN, valueFromExpression(lessThan.bound()), false);
            case Between<?> between -> new ParamValue(Condition.BETWEEN,
                    List.of(valueFromExpression(between.lowerBound()), valueFromExpression(between.upperBound())), false);
            case EqualTo<?> equalTo -> new ParamValue(Condition.EQUALS, valueFromExpression(equalTo.expression()), false);
            case Like like -> new ParamValue(Condition.LIKE, valueFromExpression(like.pattern()), false);
            case In<?> in -> new ParamValue(Condition.IN, in.expressions().stream().map(RepositoryReflectionUtils::valueFromExpression).toList(), false);
            // Negate conditions
            case jakarta.data.constraint.NotBetween<?> notBetween -> new ParamValue(Condition.BETWEEN,
                    List.of(valueFromExpression(notBetween.lowerBound()), valueFromExpression(notBetween.upperBound())), true);
            case NotEqualTo<?> notEqualTo -> new ParamValue(Condition.EQUALS, valueFromExpression(notEqualTo.expression()), true);
            case jakarta.data.constraint.NotIn<?> notIn -> new ParamValue(Condition.IN,  notIn.expressions().stream()
                    .map(RepositoryReflectionUtils::valueFromExpression).toList(), true);
            case jakarta.data.constraint.NotLike notLike -> new ParamValue(Condition.LIKE, valueFromExpression(notLike.pattern()), true);
            default ->
                    throw new UnsupportedOperationException("The FindBy annotation does not support this constraint: " + constraint.getClass()
                            + " at the Is annotation, please use one of the following: "
                            + "AtLeast, AtMost, GreaterThan, LesserThan, Between, EqualTo, Like, In, NotBetween, NotEquals, NotIn or NotLike");
        };
    }`


```