

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
    {key: 'duration', label: 'Duration'},
];
                              
var packageSummaryHeaders = [
    {key: 'key', label: 'Package Name'},
    {key: 'passed', label: 'Passed'},
    {key: 'skipped', label: 'Skipped'},
    {key: 'failed', label: 'Failed'},
    {key: 'errored', label: 'Errored'}
];
var classSummaryHeaders = [
    {key: 'key', label: 'Class Name'},
    {key: 'passed', label: 'Passed'},
    {key: 'skipped', label: 'Skipped'},
    {key: 'failed', label: 'Failed'},
    {key: 'errored', label: 'Errored'}
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


totals.percent = 100 * Number(totals.passed) / (Number(totals.passed) + Number(totals.failed) + Number(totals.errored));

ReactDOM.render(
    <Tabs   
        titles={['Summary', 'Packages', 'Classs', 'Tests', 'System Properties']}
    >
        <div className="totals">
            <span className="totalCounts">
                <table>
                <tr><td>passed:</td><td className={totals.percent == 100 ? "g" : "r"}>{totals.passed}</td></tr>
                <tr><td>skipped:</td><td className={totals.skipped == 0 ? "g" : "a"}>{totals.skipped}</td></tr>
                <tr><td>failed:</td><td className={totals.failed == 0 ? "g" : "r"}>{totals.failed}</td></tr>
                <tr><td>errored:</td><td className={totals.errored == 0 ? "g" : "r"}>{totals.errored}</td></tr>
                </table>
            </span><span className="totalPercent">
                <div>Success Rate: <span className={totals.percent == 100 ? "g" : "r"}>{totals.percent}%</span></div>
            </span>
        </div>
        <JsonTable rows={packageSummary} columns={packageSummaryHeaders} />
        <JsonTable rows={classSummary} columns={classSummaryHeaders} />
        <JsonTable rows={tests} columns={testHeaders} />
        <JsonTable rows={sysprops} columns={propertiesHeaders} />
    </Tabs>, 
        document.getElementById('results'));

