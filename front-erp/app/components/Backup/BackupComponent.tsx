// components/BackupButton.js
'use client';

import { BackupService } from '@/service/BackupService';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import { useEffect, useMemo, useRef, useState } from 'react';

const BackupComponent = () => {
    const [isLoading, setIsLoading] = useState(false);
    const toast = useRef<Toast>(null);
    const backupService = useMemo(() => new BackupService(), []);

    useEffect(() => {
        realizaBackupDiario();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    // Função que verifica e executa o backup diário
    const realizaBackupDiario = () => {
        let lastBackupDate = localStorage.getItem('lastBackupDate');
        let today = new Date().toISOString().split('T')[0];

        if (lastBackupDate === today) {
            console.debug("Backup automático para hoje já foi realizado.");
            return;
        }

        backupService.realizarBackup().then((response) => {
            localStorage.setItem('lastBackupDate', today);
            toast.current?.show({
                severity: 'info',
                summary: 'Sucesso!',
                detail: 'Backup diário realizado com sucesso! Verifique sua pasta no drive.'
            });
        }).catch((error) => {
            toast.current?.show({
                severity: 'error',
                summary: 'Erro!',
                detail: 'Não foi possível realizar o backup diário. Tente realizar manualmente.'
            });
        })
    }

    const efetuarBackup = async () => {
        setIsLoading(true);

        backupService.realizarBackup().then((response) => {
            toast.current?.show({
                severity: 'info',
                summary: 'Sucesso!',
                detail: 'Backup realizado com sucesso! Verifique sua pasta no drive.'
            });
            setIsLoading(false);
        }).catch((error) => {
            console.log(error.message);
            toast.current?.show({
                severity: 'error',
                summary: 'Erro!',
                detail: 'Não foi possível realizar o backup.'
            });
            setIsLoading(false);
        })
    };

    return (
        // style={{ fontFamily: 'sans-serif', border: '1px solid #ccc', padding: '20px', borderRadius: '8px', maxWidth: '500px' }}
        <div>

            <Toast ref={toast} />
            <Button type="button" onClick={efetuarBackup} disabled={isLoading}
                style={{ background: "--yellow-900" }}
            >
                <i className="pi pi-cloud-upload" style={{ marginRight: '10px' }}></i>
                {isLoading ? <span>Processando backup...</span> : <span>Realizar backup</span>}
            </Button>
            {/* <button
                onClick={efetuarBackup}
                disabled={isLoading}
                style={{
                    padding: '12px 24px',
                    fontSize: '16px',
                    cursor: isLoading ? 'not-allowed' : 'pointer',
                    backgroundColor: isLoading ? '#a0a0a0' : '#0070f3',
                    color: 'white',
                    border: 'none',
                    borderRadius: '5px',
                    transition: 'background-color 0.2s',
                }}
            >
                {isLoading ? 'Processando Backup...' : 'Fazer Backup Agora'}
            </button> */}
        </div>
    );
}

export default BackupComponent;