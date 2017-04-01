

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
        titles={['Summary', 'Packages', 'Classs', 'Tests', 'System Properties']}
    >
        <div className="totals">
            <span className="totalCounts">
                <table>
                <tr><td>passed:</td><td className={totals.passed == totals.executed ? "g" : "r"}>{totals.passed}</td></tr>
                <tr><td>skipped:</td><td className={totals.skipped == 0 ? "" : "a"}>{totals.skipped}</td></tr>
                <tr><td>failed:</td><td className={totals.failed == 0 ? "" : "r"}>{totals.failed}</td></tr>
                <tr><td>errored:</td><td className={totals.errored == 0 ? "" : "r"}>{totals.errored}</td></tr>
                </table>
            </span><span className="totalPercent">
                <div>Success Rate: <span className={totals.passed == totals.executed ? "g" : "r"}>{100 * Number(totals.passed) / Number(totals.executed)}%</span></div>
            </span>
        </div>
        <JsonTable rows={packageSummary} columns={packageSummaryHeaders} />
        <JsonTable rows={classSummary} columns={classSummaryHeaders} />
        <JsonTable rows={tests} columns={testHeaders} />
        <JsonTable rows={sysprops} columns={propertiesHeaders} />
    </Tabs>, 
        document.getElementById('results'));

