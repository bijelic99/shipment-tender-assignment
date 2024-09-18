const { useCallback, useState } = React;

function RatePerProvider({rate}) {
    return (
        <li>
            <div> Provider: {rate.provider.name} </div>
            <div>Total: {rate.total}</div>
            <div>Per country:
                <ul>
                    {rate.perCountry.map(pc => <li>{`${pc[0]} - ${pc[1]}`}</li>)}
                </ul>
            </div>
        </li>
    )
}


function App() {
    const [selectedFile, setSelectedFile] = useState(null)
    const [results, setResults] = useState(null)

    const handleFileChange = useCallback(
            (e) => {
                if(e.target.files) {
                    setSelectedFile(event.target.files[0])
                }
            },
            [setSelectedFile]
        )

    const handleSubmit = useCallback(
        async (e) => {
            e.preventDefault();
            const formData = new FormData()
            formData.append('file', selectedFile)
            axios
            .post('/api/shipment/analyze', formData)
            .then( response => {
                setResults(response.data)
            })
            .catch(error => {
                  console.error('Error uploading file:', error);
                })
        },
        [selectedFile, setResults]
    )

    return (
        <div>
            <h1>Programming Assignment </h1>
            <div>
                <h2> File upload</h2>
                <form onSubmit={handleSubmit}>
                    <input id="file" type="file" required onChange={handleFileChange}/>
                    <button type="submit">Upload</button>
                </form>
            </div>
            { results && (
                <div>
                    <h2>Results</h2>
                    <div>Best total rate: {results.bestTotalRateProvider.name }</div>
                    <div>Best provider per country:
                        <ol>
                            {results.bestProviderPerCountry.map(bpc => <li>{bpc[0]} - {bpc[1].name}</li>)}
                        </ol>
                    </div>
                    <ul>
                        { results.ratesPerProvider.map(r => <RatePerProvider rate={r}/>) }
                    </ul>
                </div>
            )}
        </div>
    );
}

// Render the React component into the root element
ReactDOM.render(<App />, document.getElementById('root'));