const React = require('react');
const ReactDOM = require('react-dom');

import ReactTable from 'react-table';
import 'react-table/react-table.css';

const packageSummaryHeaders = [
    {Header: 'Package',  accessor: 'key',      width: 400},
    {Header: 'Passed',   accessor: 'passed',   width: 100, Cell: props => <span className={props.value == 0 ? "r" : "g"}>{props.value}</span>},
    {Header: 'Skipped',  accessor: 'skipped',  width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Failed',   accessor: 'failed',   width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Errored',  accessor: 'errored',  width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Duration', accessor: 'duration', width: 100}
];

const classSummaryHeaders = [
    {Header: 'Class',    accessor: 'key',      width: 400},
    {Header: 'Passed',   accessor: 'passed',   width: 100, Cell: props => <span className={props.value == 0 ? "r" : "g"}>{props.value}</span>},
    {Header: 'Skipped',  accessor: 'skipped',  width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Failed',   accessor: 'failed',   width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Errored',  accessor: 'errored',  width: 100, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Duration', accessor: 'duration', width: 100}
];

const testDataHeaders = [
    //expander logic - show when there is a stack trace or an unsuccessful message. N.B. The row will still EXPAND for output
    {Header: '', expander: true, Expander: row => row.original.stackTrace != undefined || row.original.unsuccessfulMessage != undefined ?
        <div className={row.isExpanded ? "rt-expander -open" : "rt-expander"}></div> :
        null}, //still works when the empty space is clicked!
    {Header: 'Class',    accessor: 'testClass', width: 400},
    {Header: 'Name',     accessor: 'testName',  width: 400},
    {Header: 'Status',   accessor: 'status',    width: 100, Cell: props => <span className={props.value == 'Passed' ? "g" : (props.value == 'Skipped' ? "a" : "r")}>{props.value}</span>},
    {Header: 'Duration', accessor: 'duration',  width: 100},
];

const testSuitesHeader = [
    {Header: 'Name',  accessor: 'suiteName', width: 400},
    {Header: 'Tests Run', accessor: 'totals.executed', width: 100},
    {Header: 'Tests Skipped', accessor: 'totals.skipped', width: 100}
];

const nvpHeader = [
    {Header: 'Name',  accessor: 'name', width: 400},
    {Header: 'Value',  accessor: 'value', width: 400}
];

//some "nice" pagination size options.
function pageSizeOptions(data) {
    var len = data.length;
    if (len <= 20)
        return [10, Math.min(len, 20)];
    if (len <= 50)
        return [10, 20, Math.min(len, 50)];
    if (len <= 100)
        return [10, 20, 50, Math.min(len, 100)]
    if (len <= 500)
        return [10, 20, 50, 100, Math.min(len, 500)];
    return [10, 20, 50, 100, 500, 1000]; 
}
function pageSize(data) {
    if (data.length <= 20)
        return data.length;
    return undefined; //don't set the pagesize - allow component to control it
}
function showPagination(data) {
    return data.length > 20;
}


class Tabs extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            index: 0
        };
        this.switchTab = this.switchTab.bind(this);
    }
    render() {
        return <div className="tabPane">
                <div className="tabHeaders">{
                    this.props.titles.map((title, index) => 
                        <span key={index} className={index != this.state.index ? "tabTitle" : "selectedTabTitle"}><a href="#" onClick={() => this.switchTab(index)}>{title}</a></span>
                    )}
                </div><div className="tabContent">
                {this.props.children[this.state.index]}
            </div></div>;
    }
    switchTab(newIndex) {
        this.setState({index: newIndex});
    }
}

function regexFilter(filter, row) {
    try {
        return new RegExp(filter.value).test(row[filter.id]);
    } catch (err) { //bad RegExp definition
        return true;
    }
}

class OptionedReactTable extends React.Component {
    render() {
        return <div>
            <ReactTable
                className="-striped -highlight"
                data={this.props.data}
                columns={this.props.columns}
                filterable={true}
                defaultFilterMethod={regexFilter}
                pageSizeOptions={pageSizeOptions(this.props.data)}
                pageSize={pageSize(this.props.data)}
                showPagination={showPagination(this.props.data)}
                SubComponent={this.props.SubComponent}
            />
        </div>
    }
}

class TestDetailSubcomponent extends React.Component {
    render() {
        var tabNames = [];
        var tabs = [];
        if (this.props.row.unsuccessfulMessage != undefined) {
            tabNames.push("Message");
            tabs.push(<pre>${this.props.row.unsuccessfulMessage}</pre>);
        }
        if (this.props.row.stackTrace != undefined) {
            tabNames.push("Stack Trace");
            tabs.push(<pre>${this.props.row.stackTrace}</pre>);
        }
        if (this.props.row.systemOut != undefined && this.props.row.systemOut.length != 0) {
            tabNames.push("System Out");
            tabs.push(<pre>${this.props.row.systemOut.join("\n")}</pre>);
        }
        if (this.props.row.systemErr != undefined && this.props.row.systemErr.length != 0) {
            tabNames.push("System Error");
            tabs.push(<pre>${this.props.row.systemErr.join("\n")}</pre>);
        }
        return <Tabs titles={tabNames}>{tabs}</Tabs>
    }
}

ReactDOM.render(
    <span>
        <h1>Test Results</h1>
        <Tabs   
        titles={['Summary', 'Packages', 'Classs', 'Tests', 'Test Suites', 'System Properties', 'Environment Properties']}
    >
        <div key="totals" className="totals">
            <span className="mainSummary">
                <table>
                    <tbody>
                        <tr><td>timestamp:</td><td>{totals.timestamp}</td></tr>
                        <tr><td>duration:</td><td>{totals.duration}</td></tr>
                        <tr><td>passed:</td><td className={totals.passed == totals.executed ? "g" : "r"}>{totals.passed}</td></tr>
                        <tr><td>skipped:</td><td className={totals.skipped == 0 ? "" : "a"}>{totals.skipped}</td></tr>
                        <tr><td>failed:</td><td className={totals.failed == 0 ? "" : "r"}>{totals.failed}</td></tr>
                        <tr><td>errored:</td><td className={totals.errored == 0 ? "" : "r"}>{totals.errored}</td></tr>
                    </tbody>
                </table>
            </span><span className="totalPercent">
                <div>Success Rate: <span className={totals.passed == totals.executed ? "g" : "r"}>{Math.round(10000 * Number(totals.passed) / Number(totals.executed)) / 100}%</span></div>
            </span>
        </div>
        <OptionedReactTable key="packageTotals" data={packageTotals} columns={packageSummaryHeaders} />
        <OptionedReactTable key="classTotals"   data={classTotals}   columns={classSummaryHeaders} />
        <OptionedReactTable key="tests"         data={tests}         columns={testDataHeaders} SubComponent={(row) => {return <TestDetailSubcomponent row={row.original}/>}} />
        <OptionedReactTable key="testSuites"    data={testSuites}    columns={testSuitesHeader} />
        <OptionedReactTable key="sysprops"      data={buildTimeSystemProperties} columns={nvpHeader} />
        <OptionedReactTable key="envprops"      data={buildTimeEnvironmentProperties} columns={nvpHeader} />
    </Tabs>
    </span>,
    document.getElementById('root')
);
