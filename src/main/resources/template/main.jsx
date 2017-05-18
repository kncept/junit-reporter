

// page skeleton
ReactDOM.render(
    <span>
        <h1>Test Results</h1>
        <div id="results"></div>
    </span>,
        document.getElementById('root'));

var propertiesHeaders = [
    {key: 'name', label: 'Name'},
    {key: 'value', label: 'Value'}
];

var testHeaders = [
    {key: 'testClass', label: 'Class'},
    {key: 'testName', label: 'Name'},
    {key: 'status', label: 'Status', cell: function(item){
        return <span className={item.status == 'Passed' ? "g" : (item.status == 'Skipped' ? "a" : "r")}>{item.status}</span>;
    }},
    {key: 'duration', label: 'Duration'}
];
                              
var packageSummaryHeaders = [
    {key: 'key', label: 'Package Name'},
    {key: 'passed', label: 'Passed', cell: function (item) {
        return <span className={item.passed == item.available ? "g" : "r"}>{item.passed}</span>;
    }},
    {key: 'skipped', label: 'Skipped', cell: function (item) {
        return <span className={item.skipped == 0 ? "" : "r"}>{item.skipped}</span>;
    }},
    {key: 'failed', label: 'Failed', cell: function (item) {
        return <span className={item.failed == 0 ? "" : "r"}>{item.failed}</span>;
    }},
    {key: 'errored', label: 'Errored', cell: function (item) {
        return <span className={item.errored == 0 ? "" : "r"}>{item.errored}</span>;
    }}
];
var classSummaryHeaders = [
    {key: 'key', label: 'Class Name'},
    {key: 'passed', label: 'Passed', cell: function (item) {
        return <span className={item.passed == item.available ? "g" : "r"}>{item.passed}</span>;
    }},
    {key: 'skipped', label: 'Skipped', cell: function (item) {
        return <span className={item.skipped == 0 ? "" : "r"}>{item.skipped}</span>;
    }},
    {key: 'failed', label: 'Failed', cell: function (item) {
        return <span className={item.failed == 0 ? "" : "r"}>{item.failed}</span>;
    }},
    {key: 'errored', label: 'Errored', cell: function (item) {
        return <span className={item.errored == 0 ? "" : "r"}>{item.errored}</span>;
    }}
];

var Tabs = React.createClass({
    getInitialState: function() {
        return {index: 0};
    },
    getDefaultProps: function () {
        return {
            titles: []
        }
    },
    render: function() {
        return (
            <div className="tabPane">
                <div className="tabHeaders">{
                    this.props.titles.map((title, index) => 
                        <span key={index} className={index != this.state.index ? "tabTitle" : "tabTitle selectedTabTitle"}><a href="#" onClick={() => this.switchTab(index)}>{title}</a></span>
                    )}
                </div><div className="tabContent">
                {this.props.children[this.state.index]}
            </div></div>
        );
    },
    switchTab: function(newIndex) {
      this.setState({index: newIndex});
    }
});


ReactDOM.render(
    <Tabs   
        titles={['Summary', 'Packages', 'Classs', 'Tests', 'TestSuite Properties', 'System Properties']}
    >
        <div className="totals">
            <span className="mainSummary">
                <table>
                    <tbody>
                        <tr><td>timestamp:</td><td>{summary.timestamp}</td></tr>
                        <tr><td>duration:</td><td>{summary.duration}</td></tr>
                        <tr><td>passed:</td><td className={summary.passed == summary.executed ? "g" : "r"}>{summary.passed}</td></tr>
                        <tr><td>skipped:</td><td className={summary.skipped == 0 ? "" : "a"}>{summary.skipped}</td></tr>
                        <tr><td>failed:</td><td className={summary.failed == 0 ? "" : "r"}>{summary.failed}</td></tr>
                        <tr><td>errored:</td><td className={summary.errored == 0 ? "" : "r"}>{summary.errored}</td></tr>
                    </tbody>
                </table>
            </span><span className="totalPercent">
                <div>Success Rate: <span className={summary.passed == summary.executed ? "g" : "r"}>{100 * Number(summary.passed) / Number(summary.executed)}%</span></div>
            </span>
        </div>
        <JsonTable rows={packageSummary} columns={packageSummaryHeaders} />
        <JsonTable rows={classSummary} columns={classSummaryHeaders} />
        <JsonTable rows={tests} columns={testHeaders} />
        <JsonTable rows={testSuiteProps} columns={propertiesHeaders} />
        <JsonTable rows={sysprops} columns={propertiesHeaders} />
    </Tabs>, 
        document.getElementById('results'));

