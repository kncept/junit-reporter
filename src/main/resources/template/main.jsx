

// page skeleton
ReactDOM.render(
    <span>
        <h1>Test Results</h1>
        <div id="summary"></div>
        <div id="detail"></div>
    </span>,
        document.getElementById('root'));

var testHeaders = [
    {key: 'testClass', label: 'Class'},
    {key: 'testName', label: 'Name'},
    {key: 'duration', label: 'Duration'},
];

ReactDOM.render(
    <JsonTable rows={ tests } columns={ testHeaders } />, 
    document.getElementById('detail'));
