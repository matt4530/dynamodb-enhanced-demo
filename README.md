# dynamodb-enhanced-demo


VisitsDAO using the new Enhanced DynamoDB Client

Output:

```
(Just getting) Matt has visited Guatemala 26 time(s)
(After recording) Matt has visited Guatemala 27 time(s)
(After recording) Matt has visited Provo 1 time(s)
(After deletion) Matt has visited Provo 0 time(s)
Matt has visited: [Visit{visitor='matt', location='canada', visit_count=2}, Visit{visitor='matt', location='guatemala', visit_count=27}]
Matt has also visited: [Visit{visitor='matt', location='italy', visit_count=1}]
Italy was visited by: [Visit{visitor='adam', location='italy', visit_count=1}, Visit{visitor='jerod', location='italy', visit_count=1}]
Italy was also visited by: [Visit{visitor='matt', location='italy', visit_count=1}]
```