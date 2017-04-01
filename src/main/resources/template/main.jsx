

// page skeleton
ReactDOM.render(
    <span>
        <h1>Test Results</h1>
        <div id="summary"></div>
        <div id="test-detail"></div>
    </span>,
        document.getElementById('root'));

var propertiesHeaders = [
    {key: 'name', label: 'Name'},
    {key: 'value', label: 'Value'}
];

var testHeaders = [
    {key: 'testClass', label:
        <span><div>Class</div><div><input type="text"></input></div></span>
    },

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

ReactDOM.render(
    <Tabs   
        titles={['Packages', 'Classs', 'Tests', 'System Properties']}
    >
        <JsonTable rows={packageSummary} columns={packageSummaryHeaders} />
        <JsonTable rows={classSummary} columns={classSummaryHeaders} />
        <JsonTable rows={tests} columns={testHeaders} />
        <JsonTable rows={sysprops} columns={propertiesHeaders} />
    </Tabs>, 
        document.getElementById('test-detail'));

