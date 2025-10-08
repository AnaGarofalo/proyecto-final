import React from 'react';
import { Colors } from '../../utils/Colors';
import './base-switch.css';

interface BaseSwitchProps {
  checked: boolean;
  disabled?: boolean;
  onChange?: (checked: boolean) => void;
}

export const BaseSwitch: React.FC<BaseSwitchProps> = ({
  checked,
  disabled = false,
  onChange,
}) => {
  const handleClick = () => {
    if (disabled) return;
    
    if (onChange) {
      onChange(!checked);
    }
  };

  return (
       <div
      role="switch"
      aria-checked={checked}
      tabIndex={disabled ? -1 : 0}
      onClick={handleClick}
      className={`
        base-switch 
        ${checked ? 'checked' : 'unchecked'} 
        ${disabled ? 'disabled' : ''}
      `}
    >
      <div className={`base-switch-thumb ${checked ? 'checked' : 'unchecked'}`} />
      <span className={`base-switch-label ${checked ? 'checked' : 'unchecked'}`}>
        {checked ? 'ON' : 'OFF'}
      </span>
    </div>
  );
};
