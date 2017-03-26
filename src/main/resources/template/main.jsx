

// page skeleton
ReactDOM.render(
    <span>
        <h1>Test Results</h1>
        <div id="summary"></div>
        <div id="properties-detail"></div>
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

var ShowHide = React.createClass({
    getInitialState: function () {
        return { childVisible: false };
    },
    render: function() {
        if (this.state.childVisible) {
            return (
                <div>
                    <span>{this.props.header}</span>
                    <a href="#" onClick={this.onClick} className="showhide-toggle">(click to hide)</a>
                    <div>{this.props.children}</div>
                    <a href="#" onClick={this.onClick} className="showhide-toggle">(click to hide)</a>
                </div>
            )
        } else {
            return (
                <div>
                    <span>{this.props.header}</span>
                    <a href="#" onClick={this.onClick} className="showhide-toggle">(click to show)</a>
                </div>
            )
        }
    },
    onClick: function() {
      this.setState({childVisible: !this.state.childVisible});
    }
});



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
            <div>
                <span>{
                this.props.titles.map((title, index) =>
                    <span key={index} className="tabTitle"><a href="#" onClick={() => this.switchTab(index)}>{title}</a></span>
                )
                }</span>
                {this.props.children[this.state.index]}
            </div>
        );
        
    },
    switchTab: function(newIndex) {
      this.setState({index: newIndex});
    }
});


if (sysprops.length != 0) {
    ReactDOM.render(
            <ShowHide header="System Properties">
                <JsonTable rows={sysprops} columns={propertiesHeaders} />
            </ShowHide>, 
              document.getElementById('properties-detail'));
} else  {
    ReactDOM.render(
            <div>No system properties recorded</div>,
            document.getElementById('properties-detail'));
}

//ReactDOM.render(
//    <JsonTable rows={ tests } columns={ testHeaders } />, 
//    document.getElementById('test-detail'));

ReactDOM.render(
    <Tabs   
        titles={['Packages','Classs','Tests']}
    >
        <JsonTable rows={packageSummary} columns={packageSummaryHeaders} />
        <JsonTable rows={classSummary} columns={classSummaryHeaders} />
        <JsonTable rows={tests} columns={testHeaders} />
    </Tabs>, 
        document.getElementById('test-detail'));

