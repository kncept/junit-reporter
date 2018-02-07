const React = require('react');
const ReactDOM = require('react-dom');

import ReactTable from 'react-table';
import 'react-table/react-table.css';

var packageSummaryHeaders = [
    {Header: 'Package', accessor: 'key',     maxWidth: 300},
    {Header: 'Passed',  accessor: 'passed',  maxWidth: 150, Cell: props => <span className={props.value == 0 ? "r" : "g"}>{props.value}</span>},
    {Header: 'Skipped', accessor: 'skipped', maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Failed',  accessor: 'failed',  maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Errored', accessor: 'errored', maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>}
];

var classSummaryHeaders = [
    {Header: 'Class',   accessor: 'key',     maxWidth: 300},
    {Header: 'Passed',  accessor: 'passed',  maxWidth: 150, Cell: props => <span className={props.value == 0 ? "r" : "g"}>{props.value}</span>},
    {Header: 'Skipped', accessor: 'skipped', maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Failed',  accessor: 'failed',  maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>},
    {Header: 'Errored', accessor: 'errored', maxWidth: 150, Cell: props => <span className={props.value == 0 ? ""  : "r"}>{props.value}</span>}
];

var testDataHeaders = [
    {Header: 'Class',    accessor: 'testClass', maxWidth: 300},
    {Header: 'Name',     accessor: 'testName',  maxWidth: 300},
    {Header: 'Status',   accessor: 'status',    maxWidth: 150, Cell: props => <span className={props.value == 'Passed' ? "g" : (props.value == 'Skipped' ? "a" : "r")}>{props.value}</span>},
    {Header: 'Duration', accessor: 'duration',  maxWidth: 150},
];

var propertiesHeaders = [
    {Header: 'Name', accessor: 'name', maxWidth: 300},
    {Header: 'Value', accessor: 'value'}
];

class TestDetails extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return  this.props.value.stackTrace == null ? null : <pre>{this.props.value.stackTrace}</pre>;
    }
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
                        <span key={index} className={index != this.state.index ? "tabTitle" : "tabTitle selectedTabTitle"}><a href="#" onClick={() => this.switchTab(index)}>{title}</a></span>
                    )}
                </div><div className="tabContent">
                {this.props.children[this.state.index]}
            </div></div>;
    }
    switchTab(newIndex) {
        this.setState({index: newIndex});
    }
}

ReactDOM.render(
    <span>
        <h1>Test Results</h1>
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
        <ReactTable data={packageSummary} columns={packageSummaryHeaders} />
        <ReactTable data={classSummary} columns={classSummaryHeaders} />
        <ReactTable data={tests} columns={testDataHeaders} SubComponent={(row) => {console.log(row); return <TestDetails value={row.original} />}} />
        <ReactTable data={testSuiteProps} columns={propertiesHeaders} />
        <ReactTable data={sysprops} columns={propertiesHeaders} />
    </Tabs>
    </span>,
    document.getElementById('root')
);
