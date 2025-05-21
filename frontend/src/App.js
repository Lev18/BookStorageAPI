import { useState } from 'react';
import './App.css';

function App() {
    const [prompt, setPrompt] = useState('');
    const [response, setResponse] = useState('');
    const [loading, setLoading] = useState(false);

    const sendPrompt = async () => {
        if (!prompt.trim()) return;
        setLoading(true);
        setResponse('');

        try {
            const res = await fetch('http://localhost:8080/api/llm/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ prompt }),
            });
            const text = await res.json();
            console.log(text);
            setResponse(text.answer);
        } catch (error) {
            console.log('Error from backend: ', error);
            setResponse('Error response from backend');
        } finally {
            setLoading(false);
        }
    };
    return (
    <div className="container">
        <h1>📚 Book Chat Assistant</h1>
        <textarea
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            placeholder="Describe the kind of book that you like..."
        />
        <button onClick={sendPrompt} disabled={loading}>
            {loading ? 'Thinking...' : 'Send'}
        </button>
        <div className="response">
            {response && (
                <div
                    dangerouslySetInnerHTML={{
                        __html: response.replace(/\n/g, '<br/>'),
                    }}
                ></div>
            )}
        </div>
    </div>
    );

}

export default App;
